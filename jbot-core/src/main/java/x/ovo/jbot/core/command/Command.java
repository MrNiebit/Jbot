package x.ovo.jbot.core.command;

import lombok.Value;
import org.dromara.hutool.core.text.StrUtil;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.common.enums.ContactType;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.message.entity.Message;

/**
 * 命令
 *
 * @author ovo created on 2025/02/18.
 */
@Value
public class Command {

    /** 命令 */
    String command;
    /** 参数 */
    String[] args;
    /** 消息发送者 */
    Contactable from;
    /** 消息 */
    Message message;

    /**
     * 通过文本消息创建一个命令
     *
     * @param message 消息
     * @return {@link Command }
     */
    public static Command of(Message message) {
        return new Command(message);
    }

    /**
     *
     * @param message 消息
     */
    private Command(Message message) {
        this.message = message;
        this.command = StrUtil.subBefore(message.getContent(), ' ', false);
        this.args = StrUtil.subAfter(message.getContent(), ' ', false).split(" ");
        this.from = message.getSender().getType() == ContactType.GROUP ? message.getMember() : message.getSender();
    }

    /**
     * 执行
     */
    public void execute() {
        Context.get().getCommandManager().execute(this);
    }

    /**
     * 检查消息是否是一条命令
     *
     * @param message 消息
     * @return boolean
     */
    public static boolean isCommand(String message) {
        return StrUtil.isNotBlank(message) && message.startsWith("/") && Context.get().getCommandManager().names().contains(StrUtil.subBefore(message, ' ', false));
    }

}
