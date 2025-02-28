package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import x.ovo.jbot.core.common.exception.UnknownEnumException;

/**
 * 好友请求验证操作
 *
 * @author ovo created on 2025/02/17.
 */
@Getter
@RequiredArgsConstructor
public enum VerifyOperate {

    ADD(2, "添加好友"),
    AGREE(3, "同意好友请求"),
    REJECT(4, "拒绝好友请求"),
    ;

    private final int code;
    private final String desc;

    public static VerifyOperate of(int code) {
        return switch (code) {
            case 2 -> ADD;
            case 3 -> AGREE;
            case 4 -> REJECT;
            default -> throw new UnknownEnumException("好友验证操作", code);
        };
    }

}
