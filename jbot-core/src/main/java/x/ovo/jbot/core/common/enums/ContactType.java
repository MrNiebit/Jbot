package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 联系人类型
 *
 * @author ovo created on 2025/02/17.
 */
@Getter
@RequiredArgsConstructor
public enum ContactType {

    USER("自己"),
    FRIEND("好友"),
    GROUP("群组"),
    STRANGER("陌生人"),
    OFFICIAL_ACCOUNT("公众号"),
    SPECIAL("特殊账号"),
    MEMBER("群成员"),
    UNKNOWN("未知");

    private final String desc;
}
