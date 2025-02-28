package x.ovo.jbot.core.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import x.ovo.jbot.core.common.enums.ContactType;

/**
 * 联系人
 *
 * @author ovo created on 2025/02/17.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Contactable {

    /** id */
    protected String id;
    /** 昵称 */
    protected String nickname;
    /** 备注 */
    protected String remark;
    /** 类型 */
    protected ContactType type;
    /** 头像url */
    protected String avatar;

}
