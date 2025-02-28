package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 表情信息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmoteMessage extends Message {

    {this.type = MessageType.EMOTICON;}

    private String md5;
    private String desc;
    private String aesKey;

    @Override
    public String getContent() {
        return this.md5;
    }
}
