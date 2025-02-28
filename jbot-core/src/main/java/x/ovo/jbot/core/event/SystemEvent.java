package x.ovo.jbot.core.event;

/**
 * 系统事件
 *
 * @author ovo created on 2025/02/18.
 */
public class SystemEvent extends Event<String> {
    public SystemEvent(String data) {
        super(data);
    }

    public static SystemEvent of(String data) {
        return new SystemEvent(data);
    }
}
