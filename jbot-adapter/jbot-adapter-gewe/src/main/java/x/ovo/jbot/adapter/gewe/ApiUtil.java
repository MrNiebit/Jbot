package x.ovo.jbot.adapter.gewe;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.common.exception.ApiException;

/**
 * SDK模块的api请求工具
 *
 * @author ovo, created by 2024/11/24
 */
@Slf4j
@UtilityClass
public final class ApiUtil {

    private static final HttpClient HTTP_CLIENT;

    private static @Setter String header = "";
    private static @Setter String deviceId = "";
    public static final String PREFIX = "/v2/api";

    static {
        var options = new HttpClientOptions()
                .setDefaultHost(GeweAdapter.getConfig().getString("host"))
                .setDefaultPort(GeweAdapter.getConfig().getInteger("port"));
        HTTP_CLIENT = Context.vertx.createHttpClient(options);

        log.info("api连接工具初始化完成, host: {}, port: {}", options.getDefaultHost(), options.getDefaultPort());
    }

    public static Future<JsonObject> get(String path) {
        var promise = Promise.<JsonObject>promise();
        var options = new RequestOptions().addHeader("X-GEWE-TOKEN", header).setMethod(HttpMethod.GET).setURI(path);
        HTTP_CLIENT.request(options)
                .onFailure(e -> {
                    log.trace("request: GET [{}]\nfailure: {}", path, e.getMessage());
                    promise.fail(new ApiException(path + " 请求连接失败", e));
                })
                .onSuccess(request -> request.send()
                        .onFailure(e -> {
                            log.trace("request: GET [{}]\nfailure: {}", path, e.getMessage());
                            promise.fail(new ApiException(path + " 请求失败"));
                        })
                        .onSuccess(response -> response.bodyHandler(b -> {
                            JsonObject data = b.toJsonObject();
                            log.trace("request: GET [{}]\nresponse: {}", path, data);
                            promise.complete(data);
//                            if (data.getInteger("ret") == 0) {
//                                promise.complete(data.getJsonObject("data"));
//                            } else {
//                                promise.fail(new ApiException(data.getString("msg")));
//                            }
                        }))
                );
        return promise.future();
    }


    public static Future<JsonObject> post(String path, JsonObject body) {
        // 向body添加appid
        body.put("appId", deviceId);
        // 创建请求选项
        var option = new RequestOptions()
                .setURI(PREFIX + path)
                .addHeader("X-GEWE-TOKEN", header)
                .addHeader("Content-Type", "application/json")
                .setMethod(HttpMethod.POST);
        return HTTP_CLIENT.request(option)
                .compose(request -> request.send(body.toBuffer()))
                .compose(HttpClientResponse::body)
                .compose(buffer -> Future.<JsonObject>future(promise -> {
                    var data = buffer.toJsonObject();
                    if (data.getInteger("ret") == 200) promise.complete(data);
                     else promise.fail(new ApiException(data.getString("msg")));
                }))
                .onFailure(e -> log.debug("request: POST [{}] {}\nfailure: {}", path, body, e.getMessage()));
    }
}
