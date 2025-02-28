package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import x.ovo.jbot.core.common.exception.UnknownEnumException;

/**
 * 登录检查状态
 *
 * @author ovo created on 2025/02/18.
 */
@Getter
@RequiredArgsConstructor
public enum LoginCheckStatus {

    NOT_SCAN(0, "未扫码"),
    SCAN_NOT_CONFIRM(1, "已扫码，未确认"),
    LOGIN_SUCCESS(2, "登录成功"),
    ;

    private final int code;
    private final String desc;

    public static LoginCheckStatus of(int code) {
        return switch (code) {
            case 0 -> NOT_SCAN;
            case 1 -> SCAN_NOT_CONFIRM;
            case 2 -> LOGIN_SUCCESS;
            default -> throw new UnknownEnumException("登录检测状态", code);
        };
    }

}
