package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.io.file.FileUtil;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.message.Downloadable;

/**
 * 文件消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileMessage extends AppMessage implements Downloadable {

    {this.type = MessageType.APPMSG_FILE_ATTACH;}

    private String name;
    private int size;
    private String ext;
    private String md5;
    private String fileUrl;
    private String aesKey;

    @Override
    public int getFileType() {
        return 5;
    }

    @Override
    public String getContent() {
        return String.format("%s %s", this.name, FileUtil.readableFileSize(this.size));
    }
}
