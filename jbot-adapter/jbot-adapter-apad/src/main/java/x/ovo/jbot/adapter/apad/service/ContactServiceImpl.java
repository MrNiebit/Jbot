package x.ovo.jbot.adapter.apad.service;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import x.ovo.jbot.adapter.apad.APadAdapter;
import x.ovo.jbot.adapter.apad.ApiUtil;
import x.ovo.jbot.core.common.enums.VerifyOperate;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.service.ContactService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public enum ContactServiceImpl implements ContactService {
    INSTANCE;

    @Override
    public Future<Collection<Contactable>> list() {
        var body = JsonObject.of(
                "CurrentChatroomContactSeq", 0,
                "CurrentWxcontactSeq", 0,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return this.getList(new ArrayList<>(), body)
                .compose(this::getInfo)
                .onSuccess(l -> log.info("联系人加载完毕，共 {} 位联系人", l.size()));
    }

    private Future<Collection<String>> getList(List<String> wxids, JsonObject body) {
        return ApiUtil.post("/GetContractList", body)
                .map(res -> res.getJsonObject("Data"))
                .compose(data -> {
                    wxids.addAll(data.getJsonArray("ContactUsernameList").stream().map(String.class::cast).toList());
                    body.put("CurrentWxcontactSeq", data.getInteger("CurrentWxcontactSeq"));
                    body.put("CurrentChatroomContactSeq", data.getInteger("CurrentChatroomContactSeq"));
                    return data.getInteger("CountinueFlag") == 1 ? this.getList(wxids, body) : Future.succeededFuture(wxids);
                });
    }

    @Override
    public Future<JsonObject> search(String info) {
        return Future.failedFuture("暂不支持搜索好友");
    }

    @Override
    public Future<Collection<Contactable>> getInfo(Collection<String> ids) {
        final int batchSize = 15;

        var list = IntStream.iterate(0, i -> i < ids.size(), i -> i + batchSize)
                .mapToObj(i -> CollUtil.sub(ids, i, Math.min(i + batchSize, ids.size())))
                .map(batch -> CollUtil.join(batch, ","))
                .map(batch -> ApiUtil.post("/GetContact", JsonObject.of("RequestWxids", batch, "Wxid", APadAdapter.getConfig().getString("wxid")))
                        .compose(res -> {
                            List<? extends Future<? extends Contactable>> futures = res.getJsonObject("Data").getJsonArray("ContactList").stream()
                                    .map(JsonObject.class::cast)
                                    .filter(data -> StrUtil.isNotBlank(data.getJsonObject("UserName").getString("string")))
                                    .map(ContactFactory::of)
                                    .toList();
                            return Future.all(futures).map(CompositeFuture::<Contactable>list);
                        })
                ).toList();
        return Future.all(list).map(composite -> composite.<List<Contactable>>list().stream().flatMap(List::stream).toList());
    }

    @Override
    public Future<Boolean> add(int scene, int operation, String v3, String v4, String content) {
        var body = JsonObject.of(
                "Scene", scene,
                "V1", v3,
                "V2", v4,
                "VerifyContent", content,
                "Wxid", APadAdapter.getConfig().getString("wxid")
        );
        return switch (VerifyOperate.of(operation)) {
            case ADD ->
                    ApiUtil.post("/SendFriendRequest", body).map(res -> res.getJsonObject("Data").getInteger("Code") == 0);
            case AGREE ->
                    ApiUtil.post("//AcceptFriend", body).map(res -> res.getJsonObject("Data").getInteger("Code") == 0);
            default -> Future.failedFuture("暂不支持该操作");
        };
    }

    @Override
    public Future<Boolean> delete(String id) {
        return Future.failedFuture("暂不支持删除好友");
    }

    @Override
    public Future<Boolean> setRemark(String id, String remark) {
        return Future.failedFuture("暂不支持修改备注");
    }

    @Override
    public Future<Boolean> setPermission(String id, boolean chatOnly) {
        return Future.failedFuture("暂不支持修改权限");
    }
}
