package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 音乐消息
 *
 * @author ovo created on 2025/02/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MusicMessage extends AppMessage{

    {this.type = MessageType.APPMSG_MUSIC;}

    private String appid;
    private String dataUrl;
    private String coverImgUrl;
    private String songLyric;

    @Override
    public String getContent() {
        return this.title + " - " + this.desc;
    }
}
