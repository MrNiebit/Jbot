package x.ovo.jbot.adapter.gewe.service;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import x.ovo.jbot.adapter.gewe.ApiUtil;
import x.ovo.jbot.adapter.gewe.ContactFactory;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.service.ContactService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 联系人相关服务实现
 *
 * @author ovo created on 2025/02/24.
 */
@Slf4j
public enum ContactServiceImpl implements ContactService {
    INSTANCE;

    @Override
    public Future<Collection<Contactable>> list() {
        return ApiUtil.post("/contacts/fetchContactsList", JsonObject.of())
                .compose(res -> {
                    var list = new ArrayList<String>();
                    var data = res.getJsonObject("data");
                    data.getJsonArray("friends").stream().map(String.class::cast).forEach(list::add);
                    data.getJsonArray("chatrooms").stream().map(String.class::cast).forEach(list::add);
                    data.getJsonArray("ghs").stream().map(String.class::cast).forEach(list::add);
                    return this.getInfo(list).onSuccess(l -> log.info("联系人加载完毕，共 {} 位联系人", l.size()));
                });
    }

    @Override
    public Future<JsonObject> search(String info) {
        return ApiUtil.post("/contacts/search", JsonObject.of("contactsInfo", info))
                .compose(res -> Future.succeededFuture(res.getJsonObject("data")));
    }

    @Override
    public Future<Collection<Contactable>> getInfo(Collection<String> ids) {
        final int batchSize = 100;

        var list = IntStream.iterate(0, i -> i < ids.size(), i -> i + batchSize)
                .mapToObj(i -> CollUtil.sub(ids, i, Math.min(i + batchSize, ids.size())))
                .map(batch -> ApiUtil.post("/contacts/getBriefInfo", JsonObject.of("wxids", batch))
                        .compose(res -> {
                            List<? extends Future<? extends Contactable>> futures = res.getJsonArray("data").stream()
                                    .map(JsonObject.class::cast)
                                    .map(ContactFactory::of)
                                    .toList();
                            return Future.all(futures).map(CompositeFuture::<Contactable>list);
                        })
                )
                .toList();
        return Future.all(list).compose(composite -> {
            List<Contactable> contacts = composite.<List<Contactable>>list().stream()
                    .flatMap(List::stream)
                    .toList();
            return Future.succeededFuture(contacts);
        });
    }

    @Override
    public Future<Boolean> add(int scene, int operation, String v3, String v4, String content) {
        return ApiUtil.post("/contacts/addContacts", JsonObject.of("scene", scene, "option", operation, "v3", v3, "v4", v4, "content", content))
                .compose(res -> Future.succeededFuture(true));
    }

    @Override
    public Future<Boolean> delete(String id) {
        return ApiUtil.post("/contacts/deleteFriend", JsonObject.of("wxid", id))
                .compose(res -> Future.succeededFuture(true));
    }

    @Override
    public Future<Boolean> setRemark(String id, String remark) {
        return ApiUtil.post("/contacts/setFriendRemark", JsonObject.of("wxid", id, "remark", remark))
                .compose(res -> Future.succeededFuture(true));
    }

    @Override
    public Future<Boolean> setPermission(String id, boolean chatOnly) {
        return ApiUtil.post("/contacts/setFriendPermissions", JsonObject.of("wxid", id, "onlyChat", chatOnly))
                .compose(res -> Future.succeededFuture(true));
    }
}
