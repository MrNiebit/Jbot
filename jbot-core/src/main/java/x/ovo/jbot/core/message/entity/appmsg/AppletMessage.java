package x.ovo.jbot.core.message.entity.appmsg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 小程序消息
 *
 * @author ovo created on 2025/02/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppletMessage extends AppMessage {

    {this.type = MessageType.APPMSG_APPLET;}

    /** appid */
    private String appid;
    /** 应用名称 */
    private String appName;
    /** 应用图标 URL */
    private String appIconUrl;
    /** 封面图片url */
    private String coverImgUrl;
    /** 页面路径 */
    private String pagePath;
    /** 所有者 */
    private String owner;

    @Override
    public String getContent() {
        return this.appName;
    }
}
