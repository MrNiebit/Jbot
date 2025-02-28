package x.ovo.jbot.core.message.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import x.ovo.jbot.core.common.enums.MessageType;

/**
 * 位置消息
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PositionMessage extends Message{

    {this.type = MessageType.POSITION;}

    private String latitude;
    private String longitude;
    private String scale;
    private String poiName;
    private String poiId;
    private String label;

    @Override
    public String getContent() {
        return String.format("%s(%s)", this.poiName, this.label);
    }
}
