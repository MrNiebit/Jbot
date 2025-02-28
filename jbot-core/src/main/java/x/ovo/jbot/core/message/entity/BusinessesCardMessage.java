package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 企微名片消息
 *
 * @author ovo created on 2025/01/10.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessesCardMessage extends Message {

    {this.type = MessageType.BUSINESSES_CARD;}

    private String userId;
    private String nickname;
    private Gender gender;
    private String desc;

    private String v3 = this.userId;
    private String v4;

    @Override
    public String getContent() {
        return "[" + this.desc + "] " + this.nickname;
    }

}
