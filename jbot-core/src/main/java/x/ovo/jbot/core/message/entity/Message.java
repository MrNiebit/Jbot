package x.ovo.jbot.core.message.entity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import org.dromara.hutool.core.text.StrUtil;
import org.slf4j.Logger;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.common.enums.ContactType;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.contact.Member;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
public abstract class Message implements Serializable {

    protected Long id;
    protected MessageType type;
    protected JsonObject raw;
    protected String content;
    protected Long createTime;

    protected Contactable sender;
    protected Contactable receiver;
    protected Member member;

    public boolean isGroup() {
        return (Objects.nonNull(this.sender) && this.sender.getType() == ContactType.GROUP)
                || (Objects.nonNull(this.receiver) && this.receiver.getType() == ContactType.GROUP);
//                || raw.getString("FromUserName").endsWith("@chatroom")
//                || raw.getString("ToUserName").endsWith("@chatroom");
    }

    public String formatString() {
        var senderName = this.isGroup() ? this.format(this.member) : this.format(this.sender);
        var receiverName = this.isGroup() ? this.format(this.sender) : this.format(this.receiver);
        return String.format("%s -> %s: [%s] %s", senderName, receiverName, this.type.getDesc(), this.getContent());
    }


    private String format(Contactable contact) {
        if (Objects.isNull(contact)) return null;
        var nickname = contact.getNickname();
        var remark = contact instanceof Member member ? member.getDisplayName() : contact.getRemark();
        return StrUtil.format("{}{}", nickname, StrUtil.isNotBlank(remark) ? StrUtil.format("<{}>", remark) : "");
    }

    public Consumer<Logger> onSend(boolean result) {
        return logger -> logger.info("发送消息: [{}] {}", result?"✔️":"❌", this.formatString());
    }

    public Future<SentMessage> send() {
        return Context.get().getMessageManager().send(this);
    }

    public Future<SentMessage> send(Contactable contact) {
        this.setReceiver(contact);
        this.setSender(Context.get().getBot());
        return Context.get().getMessageManager().send(this);
    }

}
