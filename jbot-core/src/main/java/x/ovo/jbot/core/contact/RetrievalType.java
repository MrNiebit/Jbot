package x.ovo.jbot.core.contact;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import x.ovo.jbot.core.common.exception.UnknownEnumException;

/**
 * 联系人检索类型
 *
 * @author ovo created on 2025/02/18.
 */
@Getter
@RequiredArgsConstructor
public enum RetrievalType {

    NICK_NAME("nickname"),
    USER_NAME("username"),
    REMARK_NAME("remark");

    private final String desc;

    public static RetrievalType of(String desc) {
        return switch (desc) {
            case "nickname" -> NICK_NAME;
            case "username" -> USER_NAME;
            case "remark" -> REMARK_NAME;
            default -> throw new UnknownEnumException("联系人检索类型", desc);
        };
    }
}
