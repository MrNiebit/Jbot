package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * @author ovo created on 2025/02/27.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusNotifyMessage extends Message{

    {this.type = MessageType.STATUSNOTIFY;}

}
