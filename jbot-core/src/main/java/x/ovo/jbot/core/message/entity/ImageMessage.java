package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.io.file.FileUtil;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.message.Downloadable;

/**
 * 图片消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageMessage extends Message implements Downloadable {

    {this.type = MessageType.IMAGE;}

    private String aesKey;
    private int size;
    private String fileUrl;
    private String md5;
    private String hashBase64;

    @Override
    public int getFileType() {
        return 0;
    }

    @Override
    public String getContent() {
        return FileUtil.readableFileSize(this.size);
    }
}
