package x.ovo.jbot.plugin.model;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 11:12
 **/
@Data
public class SubscribeModel {

    /**
     * id
     */
    private String taskId;

    /**
     * @see x.ovo.jbot.plugin.enmus.CloudPanEnums
     */
    private String type;

    /**
     * 资源标识，id或名称
     */
    private String resourceIdentifier;

    /**
     * 星期几
     */
    private DayOfWeek dayOfWeek;

    /**
     * 时间
     */
    private String cronTime;

    /**
     * 资源链接
     */
    private String resourceUrl;

    /**
     * 执行 间隔分钟
     */
    private int intervalMinutes = 5;

    /**
     * 今天是否已经处理过了
     */
    private boolean processed = false;

    /**
     * 发送者id
     */
    private String senderId;
}
