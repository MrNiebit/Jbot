package x.ovo.jbot.adapter.gewe.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.net.NetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.qrcode.QrCodeUtil;
import org.dromara.hutool.extra.qrcode.QrConfig;
import x.ovo.jbot.adapter.gewe.ApiUtil;
import x.ovo.jbot.adapter.gewe.GeweAdapter;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.common.constant.JBotFiles;
import x.ovo.jbot.core.common.enums.ContactType;
import x.ovo.jbot.core.common.enums.Gender;
import x.ovo.jbot.core.common.enums.QrcodeCheckStatus;
import x.ovo.jbot.core.contact.Friend;
import x.ovo.jbot.core.domain.dto.CheckLoginDTO;
import x.ovo.jbot.core.domain.dto.QrcodeDTO;
import x.ovo.jbot.core.event.LoginSystemEvent;
import x.ovo.jbot.core.service.LoginService;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 登录服务实现
 *
 * @author ovo created on 2025/02/23.
 */
@Slf4j
public enum LoginServiceImpl implements LoginService {
    INSTANCE;

    private final JsonObject config = GeweAdapter.getConfig();

    @Override
    public Future<Void> login() {
        if (StrUtil.isBlank(this.config.getString("token")) || StrUtil.isBlank(this.config.getString("device_id"))) {
            // 如果config内没有token或deviceId，则需要创建token与deviceId，扫码登录
            return this.getToken().compose(token -> this.ScanQrLogin());
        } else {
            // config中存在token和deviceId，则直接检查登录状态、重连
            return this.otherLogin();
        }
    }


    /**
     * 首次登录
     *
     * @return {@link Future }<{@link Void }>
     * @apiNote 首次登陆，需要获取token，并创建设备，获取二维码扫码登录
     */
    private Future<Void> ScanQrLogin() {
        return Future.<Void>future(promise -> {
                    this.getQrcode().onSuccess(dto -> {
                        // 发送事件
                        LoginSystemEvent.of("请打开链接扫描二维码登录: " + this.config.getString("qr_server", "http://api.asilu.com/qrcode/?t=") + dto.getQrData()).publish();
                        this.check(promise, dto.getUuid(), new AtomicBoolean(true));
                    });
                })
                .onFailure(t -> {
                    log.error("登录失败：{}", t.getMessage());
                    System.exit(1);
                });
    }

    /**
     * 非首次登录
     *
     * @return {@link Future }<{@link Void }>
     * @apiNote 非首次登录，先检查是否离线，如果离线，则尝试重连，如果重连失败，则弹窗登录，如果弹窗登录失败，则使用扫码登录
     */
    private Future<Void> otherLogin() {
        return this.checkOnline().compose(online -> {
            // 在线，返回
            if (online) return Future.succeededFuture();
            // 不在线，尝试重连
            return this.reConnect().recover(e -> Future.future(promise -> {
                log.warn("重连失败：{}", e.getMessage());
                // 重连失败，尝试弹窗登录
                this.dialogLogin()
                        .onSuccess(uuid -> this.check(promise, uuid, new AtomicBoolean(true)))
                        .transform((s, t) -> {
                            log.info("弹窗登录失败：{}", t.getMessage());
                            return this.ScanQrLogin();
                        });
            }));
        });
    }


    private void check(Promise<Void> promise, String uuid, AtomicBoolean flag) {
        // 定时器轮询检查登录状态
        Context.vertx.setPeriodic(5000, id -> {
            this.checkLogin(uuid)
                    .onFailure(t -> {
                        Context.vertx.cancelTimer(id);
                        log.info("检查登录状态失败：{}", t.getMessage());
                        promise.fail(t.getMessage());
                    })
                    .onSuccess(check -> {
                        log.debug(JsonObject.mapFrom(check).encodePrettily());
                        // 如果二维码到期了，取消定时器
                        if (Objects.isNull(check.getExpiredTime()) || check.getExpiredTime() <= 0) {
                            Context.vertx.cancelTimer(id);
                            log.info("二维码已过期，请重新扫码登录");
                            promise.complete();
                            return;
                        }
                        log.info("[{}] 登录状态：{}", check.getNickName(), QrcodeCheckStatus.of(check.getStatus()).getDesc());
                        // 如果登录成功，则取消定时器
                        if (check.getStatus() == 2) {
                            flag.set(false);
                            Context.vertx.cancelTimer(id);
                            promise.complete();
                        }
                    });
        });
    }

    @Override
    public Future<QrcodeDTO> getQrcode() {
        return ApiUtil.post("/login/getLoginQrCode", JsonObject.of())
                .compose(res -> Future.<QrcodeDTO>future(promise -> {
                    var data = res.getJsonObject("data");
                    var dto = QrcodeDTO.builder()
                            .uuid(data.getString("uuid"))
                            .qrData(data.getString("qrData"))
                            .imgBase64(data.getString("qrImgBase64"))
                            .deviceId(data.getString("appId"))
                            .build();
                    promise.complete(dto);
                }))
                .onSuccess(dto -> {
                    if (StrUtil.isBlank(this.config.getString("device_id"))) {
                        this.config.put("device_id", dto.getDeviceId());
                        GeweAdapter.saveConfig();
                        ApiUtil.setDeviceId(dto.getDeviceId());
                        log.info("创建设备成功，设备id：{}", dto.getDeviceId());
                    }
                    var qrStr = QrCodeUtil.generateAsAsciiArt(dto.getQrData(), QrConfig.of(50, 50));
                    // 如果是win或mac，则自动打开图片
                    if (System.getProperty("os.name").toLowerCase().contains("win") || System.getProperty("os.name").toLowerCase().contains("mac")) {
                        var file = QrCodeUtil.generate(dto.getQrData(), QrConfig.of(300, 300), new File(JBotFiles.CONFIG_DIR, "qrcode.png"));
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (Exception ignore) {}
                    }
                    log.info("请点击链接或扫描下方二维码登录：{}{}{}", this.config.getString("qr_server", "http://api.asilu.com/qrcode/?t="), dto.getQrData(), Context.get().getConfig().getLogin().getPrintQrcode() ? "\r\n" + qrStr : "");
                });
    }

    private Future<String> getToken() {
        return ApiUtil.post("/tools/getTokenId", JsonObject.of())
                .compose(res -> Future.future(promise -> {
                    var token = res.getString("token");
                    this.config.put("token", token);
                    ApiUtil.setHeader(token);
                    GeweAdapter.saveConfig();
                    log.info("获取token成功：{}", token);
                    promise.complete(token);
                }));
    }

    @Override
    public Future<CheckLoginDTO> checkLogin(String uuid) {
        return ApiUtil.post("/login/checkLogin", JsonObject.of("uuid", uuid))
                .compose(res -> Future.succeededFuture(res.getJsonObject("data").mapTo(CheckLoginDTO.class)));
    }

    @Override
    public Future<String> dialogLogin() {
        return ApiUtil.post("/login/dialogLogin", JsonObject.of())
                .compose(res -> Future.succeededFuture(res.getJsonObject("data").getString("uuid")));
    }

    @Override
    public Future<Void> reConnect() {
        return ApiUtil.post("/login/reconnection", JsonObject.of())
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<Void> logout() {
        return ApiUtil.post("/login/logout", JsonObject.of())
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<Boolean> checkOnline() {
        return ApiUtil.post("/login/checkOnline", JsonObject.of())
                .compose(res -> Future.succeededFuture(res.getBoolean("data")));
    }

    @Override
    public Future<Friend> getUserInfo() {
        return ApiUtil.post("/personal/getProfile", JsonObject.of())
                .compose(res -> Future.future(promise -> {
                    var data = res.getJsonObject("data");
                    var friend = Friend.builder()
                            .id(data.getString("wxid"))
                            .nickname(data.getString("nickName"))
                            .gender(Gender.of(data.getInteger("sex")))
                            .avatar(data.getString("smallHeadImgUrl"))
                            .signature(data.getString("signature"))
                            .country(data.getString("country"))
                            .province(data.getString("province"))
                            .city(data.getString("city"))
                            .type(ContactType.USER)
                            .build();
                    promise.complete(friend);
                }));
    }

    public Future<Void> setCallback() {
        var ip = this.config.getString("callback_ip", NetUtil.getLocalhostStrV4());
        var port = this.config.getInteger("callback_port", 8511);
        return ApiUtil.post("/tools/setCallback", JsonObject.of("token", this.config.getString("token"), "callbackUrl", ip + ":" + port))
                .compose(res -> Future.succeededFuture());
    }


}
