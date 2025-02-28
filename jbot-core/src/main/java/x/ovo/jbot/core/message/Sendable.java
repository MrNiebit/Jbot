package x.ovo.jbot.core.message;

import io.vertx.core.Future;
import x.ovo.jbot.core.contact.Contactable;

/**
 * 可发送
 *
 * @author ovo created on 2025/02/17.
 */
public interface Sendable {

    default Future<Void> send(Contactable contact) {
        // todo 调用消息管理器发送
        return Future.failedFuture("不支持发送消息到联系人");
    }

}
