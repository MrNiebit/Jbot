package x.ovo.jbot.core.message;

import io.vertx.core.Future;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.manager.Manager;
import x.ovo.jbot.core.manager.ManagerLifeCycle;
import x.ovo.jbot.core.message.entity.Message;
import x.ovo.jbot.core.message.entity.SentMessage;

import java.util.Collection;

/**
 * 消息管理器
 *
 * @author ovo created on 2025/02/17.
 */
public interface MessageManager extends Manager, ManagerLifeCycle {

    Future<SentMessage> send(Message message);

    void addReceive(Message message);

    /**
     * 添加消息集合
     *
     * @param messages 消息
     */
    void addAllReceive(Collection<Message> messages);

    /**
     * 处理消息
     */
    void handle();

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setMessageManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close(){
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setMessageManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
