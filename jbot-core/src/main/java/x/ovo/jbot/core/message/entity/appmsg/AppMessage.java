package x.ovo.jbot.core.message.entity.appmsg;

import lombok.*;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.message.entity.Message;

/**
 * App消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppMessage extends Message {

    {this.type = MessageType.APPMSG;}

    protected String title;
    protected String desc;
    protected String url;
    protected AppInfo appinfo;
    protected String xml;

    @Override
    public String getContent() {
        return String.format("标题: %s\t描述: %s\tURL: %s", this.title, this.desc, this.url);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppInfo {
        private Integer version;
        private String appname;
    }
}
