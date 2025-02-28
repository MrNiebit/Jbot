package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别
 *
 * @author ovo created on 2025/02/17.
 */
@Getter
@RequiredArgsConstructor
public enum Gender {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int code;
    private final String desc;

    public static Gender of(int code) {
        return switch (code) {
            case 1 -> MALE;
            case 2 -> FEMALE;
            default -> UNKNOWN;
        };
    }
}
