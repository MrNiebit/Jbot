package x.ovo.jbot.adapter.gewe;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.message.MessageFactory;
import x.ovo.jbot.core.message.MessageManager;

import java.util.Optional;

/**
 * 回调服务
 *
 * @author ovo created on 2025/02/23.
 */
@Slf4j
public class CallbackServer {

    private static MessageManager manager;

    public static void start() {
        Context.vertx.createHttpServer()
                .requestHandler(req -> req.bodyHandler(body -> {
                    JsonObject data = body.toJsonObject();
                    log.debug("收到消息：{}", data.encodePrettily());
                    req.response().end();
                    Optional.ofNullable(manager).orElseGet(() -> {
                        var m = Context.get().getMessageManager();
                        manager = m;
                        return m;
                    }).add(MessageFactory.convert(data.getJsonObject("Data")));
                }))
                .listen(8511)
                .onSuccess(server -> log.info("消息回调服务启动成功，监听端口: {}", server.actualPort()))
                .onFailure(err -> log.warn("消息回调服务启动失败: {}", err.getMessage()));
    }
}
