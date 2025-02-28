package x.ovo.jbot.core.message.entity;

import lombok.Data;

/**
 * 已发送消息
 *
 * @author ovo created on 2025/02/25.
 */
@Data
public class SentMessage {

    private String receiver;
    private Integer type;
    private Integer msgId;
    private Long newMsgId;
    private Long createTime;

}
