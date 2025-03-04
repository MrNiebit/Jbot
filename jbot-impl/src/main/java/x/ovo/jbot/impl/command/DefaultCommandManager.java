package x.ovo.jbot.impl.command;

import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import picocli.CommandLine;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.command.Command;
import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.command.CommandManager;
import x.ovo.jbot.core.common.enums.ContactType;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.event.ExceptionEvent;
import x.ovo.jbot.core.message.entity.TextMessage;
import x.ovo.jbot.core.plugin.Plugin;
import x.ovo.jbot.core.plugin.PluginConfig;
import x.ovo.jbot.core.plugin.PluginManager;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认命令管理器
 *
 * @author ovo created on 2025/02/18.
 */
@Slf4j
public class DefaultCommandManager implements CommandManager {

    private final PluginManager pluginManager = Context.get().getPluginManager();
    private final Map<String, CommandExecutor> container = new HashMap<>();

    @Override
    public Future<Void> onInit() throws Exception {
        log.info("命令管理器初始化完成");
        return Future.succeededFuture();
    }

    @Override
    public Future<Void> register(Plugin plugin) {
        if (Objects.isNull(plugin.getCommandExecutor())) return Future.succeededFuture();
        return Future.future(promise -> Optional.ofNullable(plugin.getCommandExecutor()).ifPresent(executor -> {
            var command = new CommandLine(executor);
            this.container.put(StrUtil.addPrefixIfNot(command.getCommandName(), "/"), executor);
            Optional.ofNullable(command.getCommandSpec().aliases())
                    .ifPresent(aliases -> Arrays.stream(aliases).forEach(alias -> this.container.put(StrUtil.addPrefixIfNot(alias, "/"), executor)));
            log.debug("插件 [{}] 注册命令执行器成功", plugin.getDescription().getName());
            promise.complete();
        }));
    }

    @Override
    public Future<Void> unregister(Plugin plugin) {
        return Future.future(promise -> Optional.ofNullable(plugin.getCommandExecutor()).ifPresent(executor -> {
            var command = new CommandLine(executor);
            this.container.remove(StrUtil.addPrefixIfNot(command.getCommandName(), "/"));
            Optional.ofNullable(command.getCommandSpec().aliases())
                    .ifPresent(aliases -> Arrays.stream(aliases).forEach(alias -> this.container.remove(StrUtil.addPrefixIfNot(alias, "/"))));
        }));
    }

    @Override
    public Future<Void> execute(Command command) {
        // 1、获取命令，如果容器中不存在该命令则返回
        if (!this.container.containsKey(command.getCommand())) return Future.failedFuture("命令不存在");
        // 获取命令消息来源
        var user = command.getFrom();
        var executor = this.container.get(command.getCommand());
        executor.setCommand(command);
        return Future.<String>future(promise -> {
                    // 判断执行权限
                    if (!this.hasPermission(command.getCommand(), user)) {
                        if (Context.get().getConfig().getCommand().getShowTip()) {
                            promise.complete(Optional.ofNullable(executor.getNoPermissionTip()).orElse(Context.get().getConfig().getCommand().getNoPermissionTip()));
                        }
                        return;
                    }

                    // 构建CommandLine并重定向输出流
                    var cmd = new CommandLine(executor);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos));
                    cmd.setOut(writer);
                    cmd.setErr(writer);
                    cmd.setExecutionExceptionHandler((ex, commandLine1, fullParseResult) -> {
                        writer.print("命令执行出现异常：" + ex.getMessage());
                        return 0;
                    });

                    // 执行命令并获取命令执行的结果
                    try (baos; writer) {
                        cmd.execute(command.getArgs());
                        String result = cmd.getParseResult().asCommandLineList().stream()
                                .map(CommandLine::getExecutionResult)
                                .filter(Objects::nonNull)
                                .map(o -> (String) o)
                                .collect(Collectors.joining("\n"));
                        writer.flush();
                        String res = StrUtil.defaultIfBlank(result, baos.toString());
                        promise.complete(StrUtil.isBlank(res) ? "命令执行成功，但是没有返回值" : res);
                    } catch (Exception e) {
                        log.error("执行指令 [{}] 时出现异常: {}", command.getMessage().getContent(), e.getMessage());
                        log.debug("执行指令 [{}] 时出现异常", command.getMessage().getContent(), e);
                        new ExceptionEvent(e).publish();
                        promise.complete(StrUtil.format("执行指令 [{}] 时出现异常: {}", command.getMessage().getContent(), e.getMessage()));
                    }
                })
                .compose(str -> {
                    var msg = new TextMessage();
                    msg.setContent(str);
                    Contactable sender = command.getMessage().getSender();
                    if (sender.getType().equals(ContactType.GROUP)) {
                        msg.setAts(user.getId());
                    }
                    return sender.send(msg);
                })
                .mapEmpty();
    }

    @Override
    public Collection<String> names() {
        return this.container.keySet();
    }

    @Override
    public Collection<CommandExecutor> list() {
        return this.container.values();
    }

    @Override
    public Collection<Contactable> permissions(String command) {
        // 从容器中获取命令执行器，并从中获取到所属的插件对象
        var plugin = this.container.get(StrUtil.addPrefixIfNot(command, "/")).getPlugin();
        // 从插件管理器中获取插件配置
        var config = Context.get().getPluginManager().getConfig(plugin.getDescription().getName());
        return Optional.ofNullable(config).map(PluginConfig::getPermissions).orElse(null);
    }

    @Override
    public Map<String, Collection<Contactable>> permissions() {
        return this.container.entrySet().stream()
                .filter(entry -> CollUtil.isNotEmpty(this.permissions(entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> this.permissions(entry.getKey())));
    }

    @Override
    public boolean hasPermission(String command, Contactable user) {
        var isOwner = Objects.equals(user.getId(), Context.get().getOwner().getId());
        var contains = Optional.ofNullable(this.permissions(command)).map(p -> p.contains(user)).orElse(false);
        return isOwner || contains;
    }

    @Override
    public void addPermission(String command, Contactable user) {
        Optional.ofNullable(this.permissions(command)).ifPresent(p -> {
            p.add(user);
            Context.get().getPluginManager().saveConfig();
        });
    }

    @Override
    public void removePermission(String command, Contactable user) {
        Optional.ofNullable(this.permissions(command)).ifPresent(p -> {
            p.remove(user);
            Context.get().getPluginManager().saveConfig();
        });
    }
}
