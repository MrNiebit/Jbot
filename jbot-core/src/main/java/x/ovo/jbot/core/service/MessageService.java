package x.ovo.jbot.core.service;

import io.vertx.core.Future;
import x.ovo.jbot.core.message.Downloadable;
import x.ovo.jbot.core.message.entity.*;
import x.ovo.jbot.core.message.entity.appmsg.*;

/**
 * 消息服务
 *
 * @author ovo created on 2025/02/25.
 */
public interface MessageService {

    /**
     * 发送消息
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    default Future<SentMessage> send(Message message) {
        return switch (message.getType()) {
            case TEXT -> this.sendText((TextMessage) message);
            case FILE -> this.sendFile((FileMessage) message);
            case IMAGE -> this.sendImage((ImageMessage) message);
            case VOICE -> this.sendVoice((VoiceMessage) message);
            case VIDEO -> this.sendVideo((VideoMessage) message);
            case EMOTICON -> this.sendEmoji((EmoteMessage) message);
            case PERSONAL_CARD -> this.sendCard((PersonalCardMessage) message);
            case APPMSG -> this.sendAppmsg((AppMessage) message);
            case APPMSG_APPLET -> this.sendApplet((AppletMessage) message);
            case APPMSG_LINK -> this.sendLink((LinkMessage) message);
            case APPMSG_MUSIC -> this.sendAppmsg((MusicMessage) message);
            default -> throw new IllegalArgumentException("未实现的发送类型：" + message.getType());
        };
    }

    /**
     * 发送文本
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendText(TextMessage message);

    /**
     * 发送文件
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendFile(FileMessage message);

    /**
     * 发送图片
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendImage(ImageMessage message);

    /**
     * 发送视频
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendVideo(VideoMessage message);

    /**
     * 发送语音
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendVoice(VoiceMessage message);

    /**
     * 发送表情图片
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendEmoji(EmoteMessage message);

    /**
     * 发送链接
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendLink(LinkMessage message);

    /**
     * 发送 appmsg
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendAppmsg(AppMessage message);

    /**
     * 发送名片
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendCard(PersonalCardMessage message);

    /**
     * 发送小程序
     *
     * @param message 消息
     * @return {@link Future }<{@link SentMessage }>
     */
    Future<SentMessage> sendApplet(AppletMessage message);

    /**
     * 撤回
     *
     * @param message 消息
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> revoke(SentMessage message);

    /**
     * 下载图片
     *
     * @param type 类型
     * @param xml  消息中的xml
     * @return {@link Future }<{@link String }> 下载链接
     */
    Future<String> downloadImage(int type, String xml);

    /**
     * 下载语音
     *
     * @param msgId 味精id
     * @param xml   消息中的xml
     * @return {@link Future }<{@link String }> 下载链接
     */
    Future<String> downloadVoice(int msgId, String xml);

    /**
     * 下载视频
     *
     * @param xml 消息中的xml
     * @return {@link Future }<{@link String }> 下载链接
     */
    Future<String> downloadVideo(String xml);

    /**
     * 下载 CDN
     *
     * @param downloadable 下载
     * @return {@link Future }<{@link String }> 下载链接
     */
    Future<String> downloadCdn(Downloadable downloadable);
}
