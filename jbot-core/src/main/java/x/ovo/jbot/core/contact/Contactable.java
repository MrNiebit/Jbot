package x.ovo.jbot.core.contact;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.Future;
import x.ovo.jbot.core.common.enums.ContactType;
import x.ovo.jbot.core.message.Sendable;
import x.ovo.jbot.core.message.entity.Message;
import x.ovo.jbot.core.message.entity.TextMessage;
import x.ovo.jbot.core.plugin.ContactableJsonDeserializer;
import x.ovo.jbot.core.plugin.ContactableJsonSerializer;

/**
 * 可联系
 *
 * @author ovo created on 2025/02/17.
 */
@JsonSerialize(using = ContactableJsonSerializer.class)
@JsonDeserialize(using = ContactableJsonDeserializer.class)
public interface Contactable {

    /**
     * 获取id
     *
     * @return {@link String }
     */
    String getId();

    /**
     * 获取昵称
     *
     * @return {@link String }
     */
    String getNickname();

    /**
     * 获取备注
     *
     * @return {@link String }
     */
    String getRemark();

    /**
     * 获取类型
     *
     * @return {@link ContactType }
     */
    ContactType getType();

    /**
     * 发送信息
     *
     * @param sendable 可发送的信息
     * @return {@link Future }<{@link Void }>
     */
    default Future<Void> send(Sendable sendable) {
        System.out.println("sendable:" + ((Message)sendable).getContent());
        return sendable.send(this);
    }

    default Future<Void> send(String content) {
        var msg = new TextMessage();
        msg.setContent(content);
        msg.setReceiver(this);
        return this.send(msg);
    }

}
