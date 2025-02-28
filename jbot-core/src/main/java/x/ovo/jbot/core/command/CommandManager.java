package x.ovo.jbot.core.command;

import io.vertx.core.Future;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.manager.Manager;
import x.ovo.jbot.core.manager.ManagerLifeCycle;
import x.ovo.jbot.core.plugin.Plugin;

import java.util.Collection;
import java.util.Map;

/**
 * 命令管理器
 *
 * @author ovo created on 2025/02/17.
 */
public interface CommandManager extends Manager, ManagerLifeCycle {

    /**
     * 注册命令执行器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> register(Plugin plugin);

    /**
     * 卸载命令执行器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unregister(Plugin plugin);

    /**
     * 执行命令
     *
     * @param command 命令
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> execute(Command command);

    /**
     * 获取所有命令名称（包含别名）
     *
     * @return {@link Collection }<{@link String }>
     */
    Collection<String> names();

    /**
     * 获取所有命令执行器
     *
     * @return {@link Collection }<{@link CommandExecutor }>
     */
    Collection<CommandExecutor> list();

    /**
     * 获取指令权限
     *
     * @param command 命令
     * @return {@link Collection }<{@link String }> 具有执行权限的联系人id集合
     */
    Collection<Contactable> permissions(String command);

    /**
     * 获取所有命令与具有执行权限的联系人id
     *
     * @return {@link Map }<{@link String }, {@link Collection }<{@link String }>> key为命令，value为具有执行权限的联系人id集合
     */
    Map<String, Collection<Contactable>> permissions();

    /**
     * 检查指令权限
     *
     * @param command 命令
     * @param user    联系人id
     * @return boolean
     */
    boolean hasPermission(String command, Contactable user);

    /**
     * 添加执行权限
     *
     * @param command 命令
     * @param user    联系人id
     */
    void addPermission(String command, Contactable user);

    /**
     * 删除执行权限
     *
     * @param command 命令
     * @param user    联系人id
     */
    void removePermission(String command, Contactable user);

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setCommandManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setCommandManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
