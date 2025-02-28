package x.ovo.jbot.core.domain.dto;

import lombok.Data;
import x.ovo.jbot.core.contact.Group;
import x.ovo.jbot.core.contact.Member;

import java.util.Collection;

/**
 * @author ovo created on 2025/02/25.
 */
@Data
public class GroupInfoDTO {
    private String owner;
    private Group group;
    private Collection<String> admins;
    private Collection<Member> members;

    public Collection<String> getMemberIds() {
        return members.stream().map(Member::getId).toList();
    }
}
