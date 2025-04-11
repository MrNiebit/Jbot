package x.ovo.jbot.plugin.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 11:29
 **/
public class VertxJsonUtil {
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        JsonArray array = new JsonArray(json);
        return array.stream()
                .map(obj -> new JsonObject((Map<String, Object>) obj).mapTo(clazz))
                .collect(Collectors.toList());
    }
}
