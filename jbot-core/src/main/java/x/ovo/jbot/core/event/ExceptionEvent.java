package x.ovo.jbot.core.event;

/**
 * 异常事件
 *
 * @author ovo created on 2025/02/18.
 */
public class ExceptionEvent extends Event<Throwable> {
    public ExceptionEvent(Throwable data) {
        super(data);
    }

    public static ExceptionEvent of(Throwable data) {
        return new ExceptionEvent(data);
    }
}
