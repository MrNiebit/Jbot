package x.ovo.jbot.core.plugin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.common.util.ContactUtil;
import x.ovo.jbot.core.contact.Contactable;

import java.io.IOException;

@Slf4j
public class ContactableJsonSerializer extends JsonSerializer<Contactable> {
    @Override
    public void serialize(Contactable contact, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(ContactUtil.toString(contact));
    }
}
