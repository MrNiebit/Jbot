package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 聊天记录消息
 *
 * @author ovo created on 2025/02/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecordMessage extends AppMessage{

    {this.type = MessageType.APPMSG_CHAT_RECORD;}


    @Override
    public String getContent() {
        return this.title;
    }
}
