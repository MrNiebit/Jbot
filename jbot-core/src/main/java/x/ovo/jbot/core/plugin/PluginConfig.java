package x.ovo.jbot.core.plugin;

import lombok.Data;
import x.ovo.jbot.core.common.util.ContactUtil;
import x.ovo.jbot.core.contact.Contactable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class PluginConfig implements Serializable {

    /** 是否启用 */
    private Boolean enabled;
    /** 优先级 */
    private Integer priority;
    /** 执行下一个 */
    private Boolean next;
    /** 插件命令执行权限 */
    private List<Contactable> permissions;

    public Map<String, Object> toMap() {
        return Map.of(
                "enabled", enabled,
                "priority", priority,
                "next", next,
                "permissions", permissions.stream().map(ContactUtil::toString).toList()
        );
    }


}
