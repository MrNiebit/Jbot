package x.ovo.jbot.plugin.util;

import x.ovo.jbot.plugin.cloudpan.CloudResourceSubscriber;
import x.ovo.jbot.plugin.enmus.CloudPanEnums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 10:43
 **/
public class ServiceProxyFactory {

    private static final Map<String, CloudResourceSubscriber> SUBSCRIBER_MAP = new ConcurrentHashMap<>();

    public static void registerSubscriber(String key, CloudResourceSubscriber subscriber) {
        SUBSCRIBER_MAP.put(key, subscriber);
    }

    public static CloudResourceSubscriber getSubscriber(String key) {
        if (!SUBSCRIBER_MAP.containsKey(key)) {
            throw new RuntimeException("subscriber not found");
        }
        return SUBSCRIBER_MAP.get(key);
    }

}
