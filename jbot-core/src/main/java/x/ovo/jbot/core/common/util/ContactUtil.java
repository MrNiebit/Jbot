package x.ovo.jbot.core.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.contact.Contact;
import x.ovo.jbot.core.contact.ContactManager;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.contact.RetrievalType;

/**
 * 联系人 Util
 *
 * @author ovo created on 2025/02/19.
 */
@Slf4j
@UtilityClass
public final class ContactUtil {

    private static final String FLAG = ":@:";

    public static String toString(Contactable contact) {
        if (contact == null) return "";
        String s = StrUtil.format("username:@:{}, nickname:@:{}, remark:@:{}", contact.getId(), contact.getNickname(), contact.getRemark());
        log.debug("序列化联系人信息成功: {}", s);
        return s;
    }

    public static Contactable fromString(String string, ContactManager manager) {
        if (StrUtil.isBlank(string) || !string.contains(FLAG)) return null;
        // 分割逗号
        for (String s : string.split(",\\s*")) {
            var arr = s.split(FLAG);
            var type = RetrievalType.of(arr[0]);
            Contactable contactable = Context.get().getContactManager().get(arr.length < 2 ? "" :arr[1], type);
            if (contactable == null) continue;
            log.debug("反序列化联系人信息：{} -> {}", string, contactable);
            return contactable;
        }
        return null;
    }

    public static void format(Contact contact) {
        // todo 格式化联系人的昵称、备注emoji
    }

}
