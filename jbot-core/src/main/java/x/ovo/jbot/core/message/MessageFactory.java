package x.ovo.jbot.core.message;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.xml.XmlUtil;
import org.w3c.dom.Element;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.common.enums.MessageType;
import x.ovo.jbot.core.common.enums.VerifyOperate;
import x.ovo.jbot.core.contact.ContactManager;
import x.ovo.jbot.core.contact.RetrievalType;
import x.ovo.jbot.core.message.entity.*;
import x.ovo.jbot.core.message.entity.appmsg.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageFactory {

    protected static final ContactManager CONTACT_MANAGER = Context.get().getContactManager();
    protected static final Map<MessageType, MessageConvertor> CONVERTORS = new HashMap<>();

    static {
        CONVERTORS.put(MessageType.TEXT, text());
        CONVERTORS.put(MessageType.IMAGE, image());
        CONVERTORS.put(MessageType.VIDEO, video());
        CONVERTORS.put(MessageType.VOICE, voice());
        CONVERTORS.put(MessageType.EMOTICON, emote());
        CONVERTORS.put(MessageType.POSITION, position());
        CONVERTORS.put(MessageType.STATUSNOTIFY, statusNotify());
        CONVERTORS.put(MessageType.PERSONAL_CARD, personalCard());
        CONVERTORS.put(MessageType.VERIFY, verify());
        CONVERTORS.put(MessageType.APPMSG, appMsg());
    }

    public static Message convert(JsonObject data) {
        preHandle(data);
        var type = MessageType.of(data.getInteger("MsgType"));
        var message = CONVERTORS.getOrDefault(type, defaulted()).convert(data);
        postHandle(message, data);
        return message;
    }

    protected static void preHandle(JsonObject data) {
        data.put("FromUserName", data.getJsonObject("FromUserName").getString("string"));
        data.put("ToUserName", data.getJsonObject("ToUserName").getString("string"));
        data.put("Content", data.getJsonObject("Content").getString("string"));
        if (data.getString("FromUserName").endsWith("@chatroom")) {
            var split = data.getString("Content").split(":\n", 2);
            data.put("Content", split[1]);
            data.put("Member", split[0]);
        }
    }

    protected static void postHandle(Message message, JsonObject data) {
        message.setRaw(data);
        message.setId(data.getLong("MsgId"));
        message.setContent(data.getString("Content"));
        message.setCreateTime(data.getLong("CreateTime"));
        message.setSender(CONTACT_MANAGER.get(data.getString("FromUserName")));
        message.setReceiver(CONTACT_MANAGER.get(data.getString("ToUserName")));
        if (message.isGroup()) {
            message.setMember(CONTACT_MANAGER.get(message.getSender().getId(), data.getString("Member"), RetrievalType.USER_NAME));
        }
    }

    protected static MessageConvertor defaulted() {
        return data -> new TextMessage();
    }


    protected static MessageConvertor text() {
        return data -> new TextMessage();
    }

    protected static MessageConvertor image() {
        return data -> {
            var msg = new ImageMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var image = XmlUtil.getElement(root, "img");
            msg.setAesKey(image.getAttribute("aeskey"));
            msg.setMd5(image.getAttribute("md5"));
            msg.setFileUrl(image.getAttribute("cdnthumburl"));
            msg.setSize(Integer.parseInt(image.getAttribute("length")));
            msg.setHashBase64(data.getJsonObject("ImgBuf").getString("buffer"));
            return msg;
        };
    }

    protected static MessageConvertor video() {
        return data -> {
            var msg = new VideoMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var video = XmlUtil.getElement(root, "videomsg");
            msg.setAesKey(video.getAttribute("aeskey"));
            msg.setMd5(video.getAttribute("md5"));
            msg.setNewMd5(video.getAttribute("newmd5"));
            msg.setFileUrl(video.getAttribute("cdnvideourl"));
            msg.setSize(Integer.parseInt(video.getAttribute("length")));
            msg.setDuration(Integer.parseInt(video.getAttribute("playlength")));
            return msg;
        };
    }

    protected static MessageConvertor voice() {
        return data -> {
            var msg = new VoiceMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var voice = XmlUtil.getElement(root, "voicemsg");
            msg.setAesKey(voice.getAttribute("aeskey"));
            msg.setFileUrl(voice.getAttribute("voiceurl"));
            msg.setDuration(Integer.parseInt(voice.getAttribute("voicelength")));
            msg.setSize(Integer.parseInt(voice.getAttribute("length")));
            return msg;
        };
    }

    protected static MessageConvertor emote() {
        return data -> {
            var msg = new EmoteMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var node = XmlUtil.getElement(root, "emoji");
            msg.setMd5(node.getAttribute("md5"));
            msg.setAesKey(node.getAttribute("aeskey"));
            return msg;
        };
    }

    protected static MessageConvertor statusNotify() {
        return data -> new StatusNotifyMessage();
    }

    protected static MessageConvertor position() {
        return data -> {
            var msg = new PositionMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var node = XmlUtil.getElement(root, "location");
            msg.setLatitude(node.getAttribute("x"));
            msg.setLongitude(node.getAttribute("y"));
            msg.setScale(node.getAttribute("scale"));
            msg.setPoiName(node.getAttribute("poiname"));
            msg.setLabel(node.getAttribute("label"));
            return msg;
        };
    }

    protected static MessageConvertor personalCard() {
        return data -> {
            var msg = new PersonalCardMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            msg.setNickname(root.getAttribute("nickname"));
            msg.setV3(root.getAttribute("username"));
            msg.setV4(root.getAttribute("antispamticket"));
            msg.setGender(Gender.of(Integer.parseInt(root.getAttribute("sex"))));
            msg.setProvince(root.getAttribute("province"));
            msg.setCity(root.getAttribute("city"));
            msg.setSignature(root.getAttribute("sign"));
            msg.setAvatar(root.getAttribute("smallheadimgurl"));
            msg.setScene(Integer.parseInt(root.getAttribute("scene")));
            return msg;
        };
    }

    protected static MessageConvertor verify() {
        return data -> {
            var msg = new VerifyMessage();
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            msg.setUserId(root.getAttribute("fromusername"));
            msg.setNickname(root.getAttribute("fromnickname"));
            msg.setAlias(root.getAttribute("alias"));
            msg.setAvatar(root.getAttribute("smallheadimgurl"));
            msg.setContent(root.getAttribute("content"));
            msg.setV3(root.getAttribute("encryptusername"));
            msg.setV4(root.getAttribute("ticket"));
            msg.setScene(Integer.parseInt(root.getAttribute("scene")));
            msg.setCountry(root.getAttribute("country"));
            msg.setProvince(root.getAttribute("province"));
            msg.setCity(root.getAttribute("city"));
            msg.setGender(Gender.of(Integer.parseInt(root.getAttribute("sex"))));
            msg.setOperate(VerifyOperate.of(Integer.parseInt(root.getAttribute("opcode"))));
            return msg;
        };
    }

    protected static MessageConvertor appMsg() {
        return data -> {
            var xml = XmlUtil.parseXml(data.getString("Content"));
            var root = XmlUtil.getRootElement(xml);
            var node = XmlUtil.getElement(root, "appmsg");
            int type = Integer.parseInt(XmlUtil.getElement(node, "type").getNodeValue());
            // 适配子类型
            var msg = switch (type) {
                case 33 -> appletMessage(node);
//                case 74 -> null;
                case 6 -> appMsgFile(node);
                case 19 -> appChatRecordMessage(node);
                case 76 -> appMsgFile(node);
//                case 5 -> null;
                case 57 -> appQuoteMessage(node);
                case 51 -> appFinderMessage(node);
                default -> new AppMessage();
            };
            msg.setTitle(XmlUtil.getElement(node, "title").getNodeValue());
            msg.setDesc(XmlUtil.getElement(node, "des").getNodeValue());
            msg.setUrl(XmlUtil.getElement(node, "url").getNodeValue());
            msg.setAppinfo(XmlUtil.xmlToBean(XmlUtil.getElement(node, "appinfo"), AppMessage.AppInfo.class));
            return msg;
        };
    }

    protected static AppMessage appMsgFile(Element node) {
        var msg = new FileMessage();
        msg.setName(XmlUtil.getElement(node, "title").getNodeValue());
        var attach = XmlUtil.getElement(node, "appattach");
        msg.setSize(Integer.parseInt(XmlUtil.getElement(attach, "totallen").getNodeValue()));
        msg.setExt(ReUtil.getGroup0("<![CDATA[(.*)]]>", XmlUtil.getElement(attach, "fileext").getNodeValue()));
        msg.setFileUrl(XmlUtil.getElement(attach, "fileuploadtoken").getNodeValue());
        msg.setMd5(ReUtil.getGroup0("<![CDATA[(.*)]]>", XmlUtil.getElement(node, "md5").getNodeValue()));
        return msg;
    }

    protected static AppletMessage appletMessage(Element node) {
        var msg = new AppletMessage();

        return msg;
    }
    
    protected static ChatRecordMessage appChatRecordMessage(Element node) {
        return new ChatRecordMessage();
    }

    protected static MusicMessage appMusicMessage(Element node) {
        var msg = new MusicMessage();
        msg.setAppid(node.getAttribute("qppid"));
        msg.setDataUrl(node.getAttribute("dataurl"));
        msg.setSongLyric(node.getAttribute("songlyric"));
        msg.setCoverImgUrl(node.getAttribute("songalbumurl"));
        return msg;
    }

    protected static QuoteMessage appQuoteMessage(Element node) {
        var msg = new QuoteMessage();
        var refer = XmlUtil.getElement(node, "refermsg");
        msg.setName(XmlUtil.getElement(refer, "displayname").getNodeValue());
        msg.setReferType(MessageType.of(Integer.parseInt(XmlUtil.getElement(refer, "type").getNodeValue())));
        msg.setRefer(XmlUtil.getElement(refer, "content").getNodeValue());
        msg.setDate(DateUtil.date(Long.parseLong(XmlUtil.getElement(refer, "createtime").getNodeValue()) * 1000));
        return msg;
    }

    protected static FinderMessage appFinderMessage(Element node) {
        var msg = new FinderMessage();
        var finder = XmlUtil.getElement(node, "finderFeed");
        msg.setNickname(XmlUtil.getElement(finder, "nickname").getNodeValue());
        msg.setFinderDesc(XmlUtil.getElement(finder, "desc").getNodeValue());
        try {
            var media = XmlUtil.getElement(XmlUtil.getElement(finder, "mediaList"), "media");
            msg.setDuration(Integer.parseInt(XmlUtil.getElement(media, "videoPlayDuration").getNodeValue()));
        } catch (Exception ignored){}
        return msg;
    }


}
