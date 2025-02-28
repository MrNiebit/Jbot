package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 系统消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMessage extends Message {

    {this.type = MessageType.SYSTEM;}

}
