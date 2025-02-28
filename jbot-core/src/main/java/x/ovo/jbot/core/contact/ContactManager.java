package x.ovo.jbot.core.contact;

import io.vertx.core.Future;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.manager.Manager;
import x.ovo.jbot.core.manager.ManagerLifeCycle;

import java.util.Collection;

/**
 * 联系人管理器
 *
 * @author ovo created on 2025/02/17.
 * @implSpec 需要重写 {@code onInit()} 方法，在加载时期通过 {@link RedisHelper} 查询redis中是否存在联系人数据，
 * 如果redis中存在缓存数据，则需要反序列化保存在实现中，否则需要通过接口查询微信服务器获取联系人数据，保存于实现并序列化后保存到redis中。
 */
public interface ContactManager extends Manager, ManagerLifeCycle {

    /**
     * 向管理器中添加一个联系人
     *
     * @param contact 联系
     */
    void add(Contactable contact);

    /**
     * 向管理器中添加一个联系人列表
     *
     * @param list 列表
     */
    void addAll(Collection<Contactable> list);

    /**
     * 向管理器中添加一个群
     * @param group   群
     * @param members 成员
     */
    void addGroup(Group group, Collection<Member> members);

    /**
     * 获取联系人
     *
     * @param id id
     * @return {@link Contactable }
     */
    Contactable get(String id);

    /**
     * 指定检索类型获取联系人
     *
     * @param name 名字
     * @param type 类型
     * @return {@link Contactable }
     */
    Contactable get(String name, RetrievalType type);


    /**
     * 根据联系人的唯一标识符移除相应的联系人对象。
     *
     * @param username 要移除的联系人的唯一标识符。
     */
    void remove(String username);

    /**
     * 根据联系人的唯一标识符和检索类型移除联系人对象。
     *
     * @param id   联系人的唯一标识符。
     * @param type 检索类型，用于指定如何移除联系人信息。
     */
    void remove(String id, RetrievalType type);

    /**
     * 获取群组成员。
     * 根据群组标识符和成员的唯一标识符以及检索类型，检索并返回群组成员对象。
     *
     * @param group 群组的唯一标识符。
     * @param id    成员的唯一标识符。
     * @param type  检索类型，用于指定如何获取群组成员信息。
     * @return {@link Member} 与给定群组标识符和成员标识符匹配的群组成员对象。
     */
    Member get(String group, String id, RetrievalType type);

    /**
     * 根据群组标识符和成员的唯一标识符和检索类型移除群组成员对象。
     *
     * @param group 群组的唯一标识符。
     * @param id    成员的唯一标识符。
     * @param type  检索类型，用于指定如何移除群组成员信息。
     */
    void remove(String group, String id, RetrievalType type);

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setContactManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setContactManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
