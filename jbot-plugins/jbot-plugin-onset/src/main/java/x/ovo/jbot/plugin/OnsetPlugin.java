package x.ovo.jbot.plugin;

import io.vertx.core.buffer.Buffer;
import lombok.NonNull;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.http.HttpUtil;
import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.event.MessageEvent;
import x.ovo.jbot.core.message.entity.TextMessage;
import x.ovo.jbot.core.plugin.Plugin;

public class OnsetPlugin extends Plugin {
    @Override
    public CommandExecutor getCommandExecutor() {
        return null;
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new EventListener<MessageEvent<TextMessage>, TextMessage>(this) {

            public static final String URL = "https://60s-api.viki.moe/v2/fabing?name=";

            @Override
            public boolean support(@NonNull MessageEvent<TextMessage> event, TextMessage source) {
                return source.getContent().split("[ @]").length == 2 && ReUtil.contains("^发病|onset[ @]", source.getContent());
            }

            @Override
            public boolean onEvent(@NonNull MessageEvent<TextMessage> event, TextMessage source) {
                var name = source.getContent().split("[ @]")[1];
                var data = Buffer.buffer(HttpUtil.get(URL + name)).toJsonObject();
                source.setContent(data.getJsonObject("data").getString("saying"));
                source.send(source.getSender());
                return false;
            }
        };
    }
}
