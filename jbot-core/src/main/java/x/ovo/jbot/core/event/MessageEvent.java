package x.ovo.jbot.core.event;

import x.ovo.jbot.core.message.entity.Message;

/**
 * 消息事件
 *
 * @author ovo created on 2025/02/18.
 */
public class MessageEvent<T extends Message> extends Event<T> {

    public MessageEvent(T data) {
        super(data);
    }

    public static <T extends Message> MessageEvent<T> of(T data) {
        return new MessageEvent<>(data);
    }

    public String getContent() {
        return this.data.getContent();
    }

}
