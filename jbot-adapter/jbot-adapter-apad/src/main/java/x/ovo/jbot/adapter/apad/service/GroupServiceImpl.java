package x.ovo.jbot.adapter.apad.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.dromara.hutool.core.date.DateUtil;
import x.ovo.jbot.adapter.apad.APadAdapter;
import x.ovo.jbot.adapter.apad.ApiUtil;
import x.ovo.jbot.core.contact.Group;
import x.ovo.jbot.core.contact.Member;
import x.ovo.jbot.core.domain.dto.GroupAnnouncementDTO;
import x.ovo.jbot.core.domain.dto.GroupInfoDTO;
import x.ovo.jbot.core.service.GroupService;

import java.util.Collection;

public enum GroupServiceImpl implements GroupService {
    INSTANCE;

    @Override
    public Future<String> create(Collection<String> ids) {
        return Future.failedFuture("暂不支持创建群聊");
    }

    @Override
    public Future<Void> modifyName(String id, String name) {
        return Future.failedFuture("暂不支持修改群聊名称");
    }

    @Override
    public Future<Void> modifyRemark(String id, String remark) {
        return Future.failedFuture("暂不支持修改群聊备注");
    }

    @Override
    public Future<Void> modifyDisplayName(String id, String displayName) {
        return Future.failedFuture("暂不支持修改群聊显示名称");
    }

    @Override
    public Future<Void> inviteMember(String id, String friends, String remark) {
        var body = JsonObject.of(
                "Chatroom", id,
                "InviteWxids", friends,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return ApiUtil.post("/InviteChatroomMember", body).mapEmpty();
    }

    @Override
    public Future<Void> deleteMember(String id, String members) {
        return Future.failedFuture("暂不支持删除群聊成员");
    }

    @Override
    public Future<Void> quit(String id) {
        return Future.failedFuture("暂不支持退出群聊");
    }

    @Override
    public Future<Void> disband(String id) {
        return Future.failedFuture("暂不支持解散群聊");
    }

    @Override
    public Future<GroupInfoDTO> getInfo(String id) {
        var body = JsonObject.of(
                "Chatroom", id,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return ApiUtil.post("/GetChatroomInfoNoAnnounce", body)
                .map(res -> res.getJsonObject("Data").getJsonArray("ContactList").getList().getFirst())
                .map(JsonObject::mapFrom)
                .compose(data -> this.getMembers(id)
                        .map(members -> {
                            var dto = new GroupInfoDTO();
                            dto.setOwner(data.getString("ChatRoomOwner"));
                            dto.setMembers(members);
                            dto.setGroup(Group.builder()
                                    .id(data.getJsonObject("UserName").getString("string"))
                                    .nickname(data.getJsonObject("NickName").getString("string"))
                                    .remark(data.getJsonObject("Remark").getString("string"))
                                    .avatar(data.getString("SmallHeadImgUrl"))
                                    .owner(members.stream().filter(member -> member.getId().equals(dto.getOwner())).findFirst().orElse(null))
                                    .build()
                            );
                            return dto;
                        })
                );
    }

    @Override
    public Future<Collection<Member>> getMembers(String id) {
        var body = JsonObject.of(
                "Chatroom", id,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return ApiUtil.post("/GetChatroomMemberDetail", body)
                .map(res -> res.getJsonObject("Data").getJsonObject("NewChatroomData").getJsonArray("ChatRoomMember"))
                .map(array -> array.stream()
                        .map(JsonObject.class::cast)
                        .map(json -> (Member) Member.builder()
                                .id(json.getString("UserName"))
                                .nickname(json.getString("NickName"))
                                .inviter(json.getString("InviterUserName"))
                                .displayName(json.getString("DisplayName"))
                                .avatar(json.getString("SmallHeadImgUrl"))
                                .build()
                        ).toList()
                );
    }

    @Override
    public Future<Collection<Member>> getMemberInfo(String id, Collection<String> members) {
        return Future.failedFuture("暂不支持获取群聊成员信息");
    }

    @Override
    public Future<GroupAnnouncementDTO> getAnnouncement(String id) {
        var body = JsonObject.of(
                "Chatroom", id,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return ApiUtil.post("/GetChatroomInfo", body)
                .map(res -> res.getJsonObject("Data"))
                .map(data -> GroupAnnouncementDTO.builder()
                        .creator(data.getString("AnnouncementEditor"))
                        .content(data.getString("Announcement"))
                        .createTime(DateUtil.date(data.getLong("AnnouncementPublishTime")))
                        .build()
                );
    }

    @Override
    public Future<Void> setAnnouncement(String id, String content) {
        return Future.failedFuture("暂不支持设置群聊公告");
    }

    @Override
    public Future<String> agreeInvite(String url) {
        return Future.failedFuture("暂不支持同意加群邀请");
    }

    @Override
    public Future<Void> addFriend(String id, String member, String content) {
        return Future.failedFuture("暂不支持添加群聊成员");
    }

    @Override
    public Future<Void> applyApprove(String id, String msgId, String msg) {
        return Future.failedFuture("暂不支持同意加群申请");
    }
}
