package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.io.file.FileUtil;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.message.Downloadable;

/**
 * 视频消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoMessage extends Message implements Downloadable {

    {this.type = MessageType.VIDEO;}

    private String aesKey;
    private String fileUrl;
    private String thumbUrl;
    private int duration;
    private int size;
    private String md5;
    private String newMd5;


    @Override
    public int getFileType() {
        return 0;
    }

    @Override
    public String getContent() {
        return String.format("时长: %d s 大小: %s", this.duration, FileUtil.readableFileSize(this.size));
    }
}
