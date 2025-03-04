package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.io.file.FileUtil;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.message.Downloadable;

/**
 * 语音消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VoiceMessage extends Message implements Downloadable {

    {this.type = MessageType.VOICE;}

    private String aesKey;
    private String fileUrl;
    private int size;
    private int duration;

    @Override
    public int getFileType() {
        return 0;
    }

    @Override
    public String getContent() {
        return String.format("时长: %d s 大小: %s", this.duration / 1000, FileUtil.readableFileSize(this.size));
    }
}
