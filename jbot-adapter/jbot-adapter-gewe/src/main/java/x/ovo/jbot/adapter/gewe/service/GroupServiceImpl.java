package x.ovo.jbot.adapter.gewe.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateUtil;
import x.ovo.jbot.adapter.gewe.ApiUtil;
import x.ovo.jbot.adapter.gewe.GeweAdapter;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.contact.Group;
import x.ovo.jbot.core.contact.Member;
import x.ovo.jbot.core.domain.dto.GroupAnnouncementDTO;
import x.ovo.jbot.core.domain.dto.GroupInfoDTO;
import x.ovo.jbot.core.service.GroupService;

import java.util.Collection;

/**
 * 群组服务实现
 *
 * @author ovo created on 2025/02/25.
 */
@Slf4j
public enum GroupServiceImpl implements GroupService {
    INSTANCE;

    private final String deviceId = GeweAdapter.getConfig().getString("device_id");

    @Override
    public Future<String> create(Collection<String> ids) {
        if (ids.size() < 2) return Future.failedFuture("至少需要2个联系人才能创建群组");
        return ApiUtil.post("/group/createChatroom", JsonObject.of("wxids", ids))
                .compose(data -> Future.succeededFuture(data.getString("chatroomId")));
    }

    @Override
    public Future<Void> modifyName(String id, String name) {
        return ApiUtil.post("/group/modifyChatroomName", JsonObject.of("chatroomId", id, "chatroomName", name))
                .compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> modifyRemark(String id, String remark) {
        return ApiUtil.post("/group/modifyChatroomRemark", JsonObject.of("chatroomId", id, "chatroomRemark", remark))
                .compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> modifyDisplayName(String id, String displayName) {
        return ApiUtil.post("/group/modifyChatroomNickNameForSelf", JsonObject.of("chatroomId", id, "nickName", displayName))
                .compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> inviteMember(String id, String friends, String remark) {
        return ApiUtil.post("/group/inviteMember", JsonObject.of("chatroomId", id, "wxids", friends, "reason", remark))
                .compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> deleteMember(String id, String members) {
        return ApiUtil.post("/group/removeMember", JsonObject.of("chatroomId", id, "wxids", members))
                .compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> quit(String id) {
        return ApiUtil.post("/group/quitChatroom", JsonObject.of("chatroomId", id)).compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<Void> disband(String id) {
        return ApiUtil.post("/group/disbandChatroom", JsonObject.of("chatroomId", id)).compose(data -> Future.succeededFuture());
    }

    @Override
    public Future<GroupInfoDTO> getInfo(String id) {
        return ApiUtil.post("/group/getChatroomInfo", JsonObject.of("chatroomId", id))
                .compose(res -> this.getMembers(id)
                        .compose(members -> {
                            var data = res.getJsonObject("data");
                            var dto = new GroupInfoDTO();
                            dto.setOwner(data.getString("chatRoomOwner"));
                            dto.setMembers(members);
                            var group = Group.builder()
                                    .id(data.getString("chatroomId"))
                                    .nickname(data.getString("nickName"))
                                    .remark(data.getString("remark"))
                                    .avatar(data.getString("smallHeadImgUrl"))
                                    .owner(members.stream().filter(member -> member.getId().equals(dto.getOwner())).findFirst().orElse(null))
                                    .build();
                            dto.setGroup(group);
                            return Future.succeededFuture(dto);
                        })
                );
    }

    @Override
    public Future<Collection<Member>> getMembers(String id) {
        return ApiUtil.post("/group/getChatroomMemberList", JsonObject.of("chatroomId", id))
                .compose(res -> {
                    var data = res.getJsonObject("data");
                    var members = data.getJsonArray("memberList").stream()
                            .map(JsonObject.class::cast)
                            .map(json -> (Member) Member.builder()
                                    .id(json.getString("wxid"))
                                    .nickname(json.getString("nickName"))
                                    .inviter(json.getString("inviterUserName"))
                                    .displayName(json.getString("displayName"))
                                    .avatar(json.getString("smallHeadImgUrl"))
                                    .build()
                            ).toList();
                    return Future.succeededFuture(members);
                });
    }

    @Override
    public Future<Collection<Member>> getMemberInfo(String id, Collection<String> members) {
        return ApiUtil.post("/group/getChatroomMemberDetail", JsonObject.of("chatroomId", id, "memberWxids", members))
                .compose(res -> {
                    var data = res.getJsonArray("data").stream()
                            .map(JsonObject.class::cast)
                            .map(json -> (Member) Member.builder()
                                    .id(json.getString("userName"))
                                    .nickname(json.getString("nickName"))
                                    .remark(json.getString("remark"))
                                    .displayName(json.getString("displayName"))
                                    .alias(json.getString("alias"))
                                    .avatar(json.getString("smallHeadImgUrl"))
                                    .gender(Gender.of(json.getInteger("sex")))
                                    .signature(json.getString("signature"))
                                    .country(json.getString("country"))
                                    .province(json.getString("province"))
                                    .city(json.getString("city"))
                                    .inviter(json.getString("inviterUserName"))
                                    .build()
                            )
                            .toList();
                    return Future.succeededFuture(data);
                });
    }

    @Override
    public Future<GroupAnnouncementDTO> getAnnouncement(String id) {
        return ApiUtil.post("/group/getChatroomAnnouncement", JsonObject.of("chatroomId", id))
                .compose(res -> {
                    var data = res.getJsonObject("data");
                    var dto = GroupAnnouncementDTO.builder()
                            .creator(data.getString("announcementEditor"))
                            .content(data.getString("announcement"))
                            .createTime(DateUtil.date(data.getLong("publishTime") * 1000))
                            .build();
                    return Future.succeededFuture(dto);
                });
    }

    @Override
    public Future<Void> setAnnouncement(String id, String content) {
        return ApiUtil.post("/group/setChatroomAnnouncement", JsonObject.of("chatroomId", id, "content", content))
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<String> agreeInvite(String url) {
        return ApiUtil.post("/group/agreeJoinRoom", JsonObject.of("url", url))
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<Void> addFriend(String id, String member, String content) {
        return ApiUtil.post("/group/addGroupMemberAsFriend", JsonObject.of("chatroomId", id, "memberWxid", member, "content", content))
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<Void> applyApprove(String id, String msgId, String msg) {
        return ApiUtil.post("/group/roomAccessApplyCheckApprove", JsonObject.of("chatroomId", id, "newMsgId", msgId, "msgContent", msg))
                .compose(res -> Future.succeededFuture());
    }
}
