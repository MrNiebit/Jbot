package x.ovo.jbot.core.message;

import io.vertx.core.json.JsonObject;
import x.ovo.jbot.core.message.entity.Message;

@FunctionalInterface
public interface MessageConvertor {

    Message convert(JsonObject data);

}
