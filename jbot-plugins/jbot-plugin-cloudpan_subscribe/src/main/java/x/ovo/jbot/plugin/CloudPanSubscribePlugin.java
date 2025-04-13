package x.ovo.jbot.plugin;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.data.id.UUID;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.json.JSONUtil;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.contact.RetrievalType;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.event.MessageEvent;
import x.ovo.jbot.core.message.entity.TextMessage;
import x.ovo.jbot.core.plugin.Plugin;
import x.ovo.jbot.plugin.cloudpan.CloudResourceSubscriber;
import x.ovo.jbot.plugin.cloudpan.impl.AliPanResourceSubscriberImpl;
import x.ovo.jbot.plugin.enmus.CloudPanEnums;
import x.ovo.jbot.plugin.model.SubscribeModel;
import x.ovo.jbot.plugin.util.CronTimeParser;
import x.ovo.jbot.plugin.util.ServiceProxyFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author gitsilence
 */
@Slf4j
public class CloudPanSubscribePlugin extends Plugin {

    private static List<SubscribeModel> subscribeModels = Collections.emptyList();

    private static final String CONFIG_JSON = System.getProperty("user.dir") + "/cloud_pan_subscribe_config.json";


    @Override
    public CommandExecutor getCommandExecutor() {
        return null;
    }

    @Override
    public void onLoad() throws Exception {
        super.onLoad();
        // 注册服务
        ServiceProxyFactory.registerSubscriber(CloudPanEnums.ALI_PAN.name(), new AliPanResourceSubscriberImpl());

        var path = java.nio.file.Paths.get(CONFIG_JSON);
        log.info("cloud pan path: {}", path);
        String content = Files.readString(path);
        subscribeModels = JSONUtil.toList(content, SubscribeModel.class);
        ThreadUtil.execute(this::monitorJob);
    }

    private void monitorJob() {
        // 启动一个每5分钟检查一次的定时任务
        java.util.Timer timer = new java.util.Timer(true);
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                log.info("订阅任务开始执行, 数量: [{}]", subscribeModels.size());
                if (subscribeModels != null) {
                    for (SubscribeModel model : subscribeModels) {
                        CloudResourceSubscriber subscriber = ServiceProxyFactory.getSubscriber(model.getType());
                        String message = subscriber.checkForUpdate(model);
                        if (StrUtil.isNotBlank(message)) {
                            // 发送消息
                            Context.get().getContactManager().get(model.getSenderId()).send(message);
                        }
                    }
                }
            }
        }, 0, 60 * 1000 * 5); // 每5分钟执行一次
    }

    public void persist() {
        try {
            var jsonArray = new io.vertx.core.json.JsonArray();
            for (var model : subscribeModels) {
                jsonArray.add(io.vertx.core.json.JsonObject.mapFrom(model));
            }
            var path = java.nio.file.Paths.get(CONFIG_JSON);
            java.nio.file.Files.writeString(path, jsonArray.encodePrettily(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new EventListener<MessageEvent<TextMessage>, TextMessage>(this) {

            @Override
            public boolean support(@NonNull MessageEvent<TextMessage> event, TextMessage source) {
                return source.getContent().startsWith("cloud_pan");
            }

            @Override
            public boolean onEvent(@NonNull MessageEvent<TextMessage> event, TextMessage source) {
                var content = source.getContent();
                String[] split = content.split("\\s+");
                String fromUserName = source.getRaw().getString("FromUserName");
                Contactable contactable = Context.get().getContactManager().get(fromUserName, RetrievalType.USER_NAME);
                if (split.length < 2) {
                    contactable.send("格式错误，请使用：cloud_pan list/delete/add ali_pan/baidu_pan url");
                    return true;
                }
                String operation = split[1];

                switch (operation) {
                    case "list": {
                        if (subscribeModels == null || subscribeModels.isEmpty()) {
                            contactable.send("暂无订阅任务");
                        } else {
                            StringBuilder sb = new StringBuilder("当前订阅任务：\n");
                            for (int i = 0; i < subscribeModels.size(); i++) {
                                var m = subscribeModels.get(i);
                                sb.append(i + 1).append(". ").append(m.getType()).append(" - ").append(m.getResourceUrl()).append("\n");
                            }
                            contactable.send(sb.toString());
                        }
                        return true;
                    }
                    case "delete": {
                        if (split.length < 3) {
                            contactable.send("格式错误，请使用：cloud_pan delete url");
                            return true;
                        }
                        String url = split[2];
                        boolean removed = subscribeModels.removeIf(m -> url.equals(m.getResourceUrl()));
                        if (removed) {
                            persist();
                            contactable.send("已删除订阅：" + url);
                        } else {
                            contactable.send("未找到订阅：" + url);
                        }
                        return true;
                    }
                    case "add": {
                        if (split.length < 5) {
                            contactable.send("格式错误，请使用：cloud_pan add ali_pan/baidu_pan url 周六下午6点 密码");
                            return true;
                        }
                        String url = split[3];
                        if (subscribeModels.stream().anyMatch(m -> url.equals(m.getResourceUrl()))) {
                            contactable.send("已存在订阅：" + url);
                            return true;
                        }
                        String type = split[2];
                        String date = split[4];
                        var cronDate = CronTimeParser.parse(date);
                        SubscribeModel model = new SubscribeModel();
                        model.setTaskId(UUID.fastUUID().toString());
                        model.setType(CloudPanEnums.findByName(split[2]).name());
                        model.setResourceUrl(url);
                        model.setDayOfWeek(cronDate.dayOfWeek());
                        model.setCronTime(cronDate.time());
                        model.setSenderId(fromUserName);
                        if (split.length == 6) {
                            model.setPassword(split[5]);
                        }
                        subscribeModels.add(model);
                        persist();
                        contactable.send("已添加订阅：" + type + " - " + url);
                        return true;
                    }
                    default: {
                        contactable.send("格式错误，请使用：cloud_pan list/delete/add ali_pan/baidu_pan url 周五下午6点");
                        return true;
                    }
                }
            }
        };
    }
}
