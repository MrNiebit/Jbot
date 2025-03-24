package x.ovo.jbot.core.plugin;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.common.util.ContactUtil;
import x.ovo.jbot.core.contact.Contactable;

import java.io.IOException;

@Slf4j
public class ContactableJsonDeserializer extends JsonDeserializer<Contactable> {

    @Override
    public Contactable deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return ContactUtil.fromString(jsonParser.getText());
    }
}
