package x.ovo.jbot.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import x.ovo.jbot.core.common.exception.UnknownEnumException;

import java.util.stream.Stream;

/**
 * 消息类型
 *
 * @author ovo created on 2025/02/17.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public enum MessageType {
    TEXT(1, "文本消息"),
    HTML(2, "HTML消息"),
    IMAGE(3, "图片消息"),
    FILE(6, "文件消息"),
    VOICE(34, "语音消息"),
    VERIFY(37, "好友请求消息"),
    POSSIBLE_FRIEND(40, "可能为好友消息"),
    PERSONAL_CARD(42, "个人名片消息"),
    VIDEO(43, "视频消息"),
    EMOTICON(47, "表情消息"),
    POSITION(48, "位置消息"),
    // 引用消息、app消息
    // 49 + 74是文件通知消息,+6是具体的文件附件消息，19-聊天记录，5-链接或入群邀请，76-音乐消息
    APPMSG(49, "APP消息"),
    APPMSG_APPLET(4933, "小程序消息"),
    APPMSG_FILE_NOTIFY(4974, "文件通知消息"),
    APPMSG_FILE_ATTACH(4906, "文件附件消息"),
    APPMSG_CHAT_RECORD(4919, "聊天记录消息"),
    APPMSG_MUSIC(4976, "音乐消息"),
    APPMSG_LINK(4905, "链接消息"),
    APPMSG_QUOTE(4957, "引用消息"),
    APPMSG_FINDER(4951, "视频号消息"),

    VOIP(50, "VoIP通话消息"),
    STATUSNOTIFY(51, "状态通知消息"),
    VOIPNOTIFY(52, "VoIP结束消息"),
    VOIPINVITE(53, "VoIP邀请消息"),
    MICROVIDEO(62, "小视频消息"),
    BUSINESSES_CARD(66, "企微名片消息"),
    SYSNOTICE(9999, "系统通知消息"),
    SYSTEM(10000, "系统消息"),
    // weixin推送的系统消息、拍一拍
    RECALLED(10002, "撤回消息"),
    ;

    private final int code;
    private final String desc;

    @SneakyThrows
    public static MessageType of(int code) {
        return Stream.of(MessageType.values()).filter(e -> e.getCode() == code).findAny().orElseThrow(() -> new UnknownEnumException("消息", code));
    }
}
