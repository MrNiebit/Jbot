package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.common.enums.VerifyOperate;

/**
 * 验证消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyMessage extends Message {

    {this.type = MessageType.VERIFY;}

    private String userId;
    private String nickname;
    private String content;
    /** 场景 30-通过扫描二维码添加好友 | 14-通过群聊添加好友(之前是好友) | 17-通过个人名片 | 3-微信号搜索 | 4-QQ好友 | 8-群聊（？ | 15-手机号 */
    private int scene;
    private String country;
    private String province;
    private String city;
    private String signature;
    private Gender gender;
    private String avatar;
    private String alias;
    private VerifyOperate operate;

    private String v3;
    private String v4;

    @Override
    public String getContent() {
        return String.format("%s: %s", this.nickname, this.content);
    }

}
