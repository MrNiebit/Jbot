package x.ovo.jbot.plugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/11 09:17
 **/
@Data
public class CloudResourceInfoModel {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 更新时间
     */
    private String updatedAt;

    /**
     * 文件大小
     */
    private Long size;

}
