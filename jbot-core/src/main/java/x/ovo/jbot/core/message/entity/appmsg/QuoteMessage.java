package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

import java.util.Date;

/**
 * 引用消息
 *
 * @author ovo created on 2025/02/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteMessage extends AppMessage{

    {this.type = MessageType.APPMSG_QUOTE;}

    private MessageType referType;
    private String name;
    private String refer;
    private Date date;

    @Override
    public String getContent() {
        return String.format("⌈[%s] %s: [%s] %s⌋ %s", this.date, this.name, this.referType, this.refer, this.title);
    }
}
