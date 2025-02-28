package x.ovo.jbot.core.plugin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 插件描述
 *
 * @author ovo created on 2025/02/17.
 */
@Data
public class PluginDescription implements Serializable {

    /** 插件名称 */
    private String name;
    /** 插件主类 */
    private String main;
    /** 版本 */
    private String version;
    /** 优先权 */
    private int priority;
    /** 描述 */
    private String description;
    /** 作者 */
    private List<String> authors;

}
