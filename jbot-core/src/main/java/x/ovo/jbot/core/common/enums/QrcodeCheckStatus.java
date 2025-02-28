package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import x.ovo.jbot.core.common.exception.UnknownEnumException;

import java.util.Arrays;

/**
 * 登录二维码检查状态
 *
 * @author ovo created on 2025/02/26.
 */
@Getter
@RequiredArgsConstructor
public enum QrcodeCheckStatus {

    WAITING(0, "等待扫码中..."),
    SCANNED(1, "已扫码，请在手机上确认登录"),
    SUCCESS(2, "登录成功"),

    TIMEOUT(3, "二维码超时，请重新获取"),
    FAILURE(5, "登录失败"),
    ;

    private final int code;
    private final String desc;

    public static QrcodeCheckStatus of(int code) {
        return Arrays.stream(values()).filter(status -> status.code == code).findFirst().orElseThrow(() -> new UnknownEnumException("登录状态", code));
    }
}
