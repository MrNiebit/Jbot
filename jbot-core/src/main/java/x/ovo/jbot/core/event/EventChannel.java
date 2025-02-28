package x.ovo.jbot.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventChannel {
    /** 系统事件 */ SYSTEM("event:system"),
    /** 登录事件 */ SYSTEM_LOGIN("event:system:login"),
    /** 注销事件 */ SYSTEM_LOGOUT("event:system:logout"),
    /** 异常事件 */ SYSTEM_ERROR("event:system:error"),

    /** 消息事件 */ MESSAGE("event:message"),
    /** 文本消息 */ MESSAGE_TEXT("event:message:text"),
    /** 图片消息 */ MESSAGE_IMAGE("event:message:image"),
    /** 语音消息 */ MESSAGE_VOICE("event:message:voice"),
    /** 视频消息 */ MESSAGE_VIDEO("event:message:video"),
    /** 文件消息 */ MESSAGE_FILE("event:message:file"),
    /** 表情消息 */ MESSAGE_EMOTION("event:message:emotion"),
    /** 未知消息 */ MESSAGE_LOCATION("event:message:location"),
    /** 连接消息 */ MESSAGE_LINK("event:message:link"),
    /** 撤回消息 */ MESSAGE_REVOKE("event:message:revoke"),

    /** 插件调用事件 */ PLUGIN_CALL("event:plugin:call:"),
    ;

    private final String desc;
}
