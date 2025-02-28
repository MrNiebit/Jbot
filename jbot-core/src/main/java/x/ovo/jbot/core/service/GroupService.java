package x.ovo.jbot.core.service;

import io.vertx.core.Future;
import x.ovo.jbot.core.contact.Member;
import x.ovo.jbot.core.domain.dto.GroupAnnouncementDTO;
import x.ovo.jbot.core.domain.dto.GroupInfoDTO;

import java.util.Collection;

public interface GroupService {

    /**
     * 创建群聊
     *
     * @param ids 好友id集合, >=2
     * @return {@link Future }<{@link String }> 群id
     */
    Future<String> create(Collection<String> ids);

    /**
     * 修改群名称
     * @param id   id
     * @param name 名字
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> modifyName(String id, String name);

    /**
     * 修改群备注
     *
     * @param id     id
     * @param remark 备注
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> modifyRemark(String id, String remark);

    /**
     * 修改显示名称（群昵称）
     *
     * @param id          id
     * @param displayName 显示名称
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> modifyDisplayName(String id, String displayName);

    /**
     * 邀请成员
     *
     * @param id      id
     * @param friends 邀请进群的好友wxid，多个英文逗号分隔
     * @param remark  邀请进群说明
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> inviteMember(String id, String friends, String remark);

    /**
     * 删除成员
     *
     * @param id      id
     * @param members 删除的群成员wxid，多个英文逗号分隔
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> deleteMember(String id, String members);

    /**
     * 退出
     *
     * @param id id
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> quit(String id);

    /**
     * 解散
     *
     * @param id id
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> disband(String id);

    /**
     * 获取信息
     *
     * @param id id
     * @return {@link Future }<{@link GroupInfoDTO }>
     */
    Future<GroupInfoDTO> getInfo(String id);

    /**
     * 获取成员列表
     *
     * @param id id
     * @return {@link Future }<{@link GroupInfoDTO }>
     */
    Future<Collection<Member>> getMembers(String id);

    /**
     * 获取成员信息
     *
     * @param id      id
     * @param members 成员
     * @return {@link Future }<{@link Collection }<{@link Member }>>
     */
    Future<Collection<Member>> getMemberInfo(String id, Collection<String> members);

    /**
     * 获取公告
     *
     * @param id id
     * @return {@link Future }<{@link GroupAnnouncementDTO }>
     */
    Future<GroupAnnouncementDTO> getAnnouncement(String id);

    /**
     * 设置公告
     *
     * @param id      id
     * @param content 内容
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> setAnnouncement(String id, String content);

    /**
     * 同意邀请
     *
     * @param url 网址
     * @return {@link Future }<{@link String }>
     */
    Future<String> agreeInvite(String url);

    /**
     * 添加群成员为好友
     *
     * @param id      id
     * @param member  成员id
     * @param content 添加好友的招呼语
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> addFriend(String id, String member, String content);

    /**
     * 同意进群申请
     *
     * @param id    id
     * @param msgId 消息id
     * @param msg   进群申请id
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> applyApprove(String id, String msgId, String msg);
}
