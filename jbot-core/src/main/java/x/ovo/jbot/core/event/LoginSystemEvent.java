package x.ovo.jbot.core.event;


/**
 * 登录系统事件
 *
 * @author ovo created on 2025/02/25.
 */
public class LoginSystemEvent extends SystemEvent{
    public LoginSystemEvent(String data) {
        super(data);
    }

    public static LoginSystemEvent of(String data) {
        return new LoginSystemEvent(data);
    }
}
