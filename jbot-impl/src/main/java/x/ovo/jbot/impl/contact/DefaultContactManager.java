package x.ovo.jbot.impl.contact;

import io.vertx.core.Future;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.convert.ConvertUtil;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.contact.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultContactManager implements ContactManager {

    /** 联系人数据：[wxid: 联系人] */
    private final Map<String, Contactable> contacts = new HashMap<>();
    /** 群组数据：[群组wxid: Coll<群成员>] */
    private final Map<String, Collection<Member>> groups = new HashMap<>();
    private RedisAPI redis;


    @Override
    public void add(Contactable contact) {
        this.contacts.put(contact.getId(), contact);
    }

    @Override
    public void addAll(Collection<Contactable> list) {
        this.contacts.putAll(list.stream().collect(Collectors.toMap(Contactable::getId, contact -> contact)));
    }

    @Override
    public void addGroup(Group group, Collection<Member> members) {
        this.groups.put(group.getId(), members);
        log.info("群组 [{}] 成员获取完毕，共 {} 个成员", group.getNickname(), members.size());
    }

    @Override
    public Future<Void> onInit() throws Exception {
        // todo
        var client = Redis.createClient(Context.vertx, Context.get().getConfig().getBot().getRedis());
        this.redis = RedisAPI.api(client);

        // 如果 redis 存在，则从redis中获取联系人数据
        this.redis.hgetall("contacts")
                .map(response -> response.attributes().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> ConvertUtil.convert(Contactable.class, entry.getValue()))))
                .onSuccess(this.contacts::putAll);

        log.info("联系人管理器初始化完成");
        return ContactManager.super.onInit();
    }

    @Override
    public Contactable get(String id) {
        return this.contacts.get(id);
    }

    @Override
    public Contactable get(String name, RetrievalType type) {
        return RetrievalType.USER_NAME == type ? this.get(name) : RetrievalStrategy.of(type).get(this.contacts.values().iterator(), name);
    }

    @Override
    public void remove(String username) {
        this.contacts.remove(username);
    }

    @Override
    public void remove(String id, RetrievalType type) {
        if (RetrievalType.USER_NAME == type) this.remove(id);
        RetrievalStrategy.of(type).remove(this.contacts.values().iterator(), id);
    }

    @Override
    public Member get(String group, String id, RetrievalType type) {
        return (Member) RetrievalStrategy.of(type).get(this.groups.get(group).iterator(), id);
    }

    @Override
    public void remove(String group, String id, RetrievalType type) {
        RetrievalStrategy.of(type).remove(this.groups.get(group).iterator(), id);
    }


    @Override
    public Future<Void> onDestroy() throws Exception {
//        this.redis.hset()
        return ContactManager.super.onDestroy();
    }
}
