package x.ovo.jbot.core.event;

import lombok.Getter;
import x.ovo.jbot.core.plugin.Plugin;

/**
 * 插件异常事件
 *
 * @author ovo created on 2025/02/18.
 */
@Getter
public class PluginExceptionEvent extends ExceptionEvent {

    private final Plugin plugin;

    public PluginExceptionEvent(Plugin plugin, Throwable data) {
        super(data);
        this.plugin = plugin;
    }

    public static PluginExceptionEvent of(Plugin plugin, Throwable data) {
        return new PluginExceptionEvent(plugin, data);
    }
}
