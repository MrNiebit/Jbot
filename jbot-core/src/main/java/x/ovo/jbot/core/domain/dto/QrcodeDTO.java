package x.ovo.jbot.core.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author ovo created on 2025/02/25.
 */
@Data
@Builder
public class QrcodeDTO {

    private String uuid;
    private String qrData;
    private String imgBase64;
    private String deviceId;

}
