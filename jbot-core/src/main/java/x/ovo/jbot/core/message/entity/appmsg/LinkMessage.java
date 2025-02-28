package x.ovo.jbot.core.message.entity.appmsg;


import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 链接消息
 *
 * @author ovo created on 2025/01/10.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMessage extends AppMessage{

    {this.type = MessageType.APPMSG_LINK;}

    @Override
    public String getContent() {
        return String.format("[%s] (%s) %s", this.title, this.desc, this.url);
    }
}
