package x.ovo.jbot.core.common.exception;

import lombok.experimental.StandardException;

/**
 * 未知枚举异常
 *
 * @author ovo created on 2025/02/17.
 */
@StandardException
public class UnknownEnumException extends RuntimeException {

    public UnknownEnumException(String type, Object o) {
        super("未知的 [" + type + "] 类型: " + o);
    }

}
