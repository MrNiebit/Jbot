package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 视频号消息
 *
 * @author ovo created on 2025/02/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FinderMessage extends AppMessage{

    {this.type = MessageType.APPMSG_FINDER;}

    private String nickname;
    private String finderDesc;
    private int duration;


    @Override
    public String getContent() {
        return String.format("时长: %d s, %s - %s", this.duration, this.nickname, this.finderDesc);
    }
}
