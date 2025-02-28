package x.ovo.jbot.core.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 群公告 DTO
 *
 * @author ovo created on 2025/02/25.
 */
@Data
@Builder
public class GroupAnnouncementDTO {

    private String content;
    private String creator;
    private Date createTime;

}
