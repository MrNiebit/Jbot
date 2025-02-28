package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 个人卡消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalCardMessage extends Message{

    {this.type = MessageType.PERSONAL_CARD;}

    private String nickname;
    private Gender gender;
    private String province;
    private String city;
    private String avatar;
    private String signature;
    private int scene;

    private String v3;
    private String v4;

    @Override
    public String getContent() {
        return this.nickname;
    }

}
