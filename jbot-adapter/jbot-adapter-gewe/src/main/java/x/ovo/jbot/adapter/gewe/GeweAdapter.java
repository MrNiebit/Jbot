package x.ovo.jbot.adapter.gewe;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.adapter.gewe.service.ContactServiceImpl;
import x.ovo.jbot.adapter.gewe.service.GroupServiceImpl;
import x.ovo.jbot.adapter.gewe.service.LoginServiceImpl;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.adapter.Adapter;
import x.ovo.jbot.core.common.constant.JBotFiles;
import x.ovo.jbot.core.service.*;

import java.io.File;

/**
 * GEWE 适配器
 *
 * @author ovo created on 2025/02/23.
 */
@Slf4j
public class GeweAdapter implements Adapter {

    private static @Getter JsonObject config;
    private static final String NAME = "gewe-adapter";

    @Override
    public Future<Void> onInit(JsonObject cfg) {
        return Future.future(promise -> {
            FileSystem fs = Context.vertx.fileSystem();
            String path = new File(JBotFiles.ADAPTER_DIR, this.name() + File.separator + "config.json").getPath();
            // 如果存在配置文件，则合并配置
            if (fs.existsBlocking(path)) cfg.mergeIn(fs.readFileBlocking(path).toJsonObject());
            log.debug("adapter config: {}", cfg.encodePrettily());
            // api工具设置token与deviceId
            GeweAdapter.config = cfg;
            ApiUtil.setHeader(cfg.getString("token"));
            ApiUtil.setDeviceId(cfg.getString("device_id"));
            log.info("Gewe adapter 初始化完成");
            promise.complete();
        });
    }

    @Override
    public Future<Void> onDestroy() {
        return saveConfig();
    }

    public static Future<Void> saveConfig() {
        String path = new File(JBotFiles.ADAPTER_DIR, NAME + File.separator + "config.json").getPath();
        return Context.vertx.fileSystem()
                .writeFile(path, Buffer.buffer(config.encodePrettily()))
                .onSuccess(v -> log.debug("配置文件保存成功"));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Future<Void> start() {
        //  1、执行登录流程或重连
        return this.getLoginService().login()
                .onSuccess(v -> log.info("登录成功"))
                .compose(v -> this.getLoginService().getUserInfo().onSuccess(me -> Context.get().getContactManager().add(me)))
                //  2、获取联系人信息存入联系人管理器中
                .compose(v -> this.getContactService().list())
                //  3、开启回调服务器
                .compose(v -> Future.<Void>succeededFuture())
                .onSuccess(v -> CallbackServer.start());
    }

    @Override
    public ContactService getContactService() {
        return ContactServiceImpl.INSTANCE;
    }

    @Override
    public DownloadService getDownloadService() {
        return null;
    }

    @Override
    public FavoriteService getFavoriteService() {
        return null;
    }

    @Override
    public GroupService getGroupService() {
        return GroupServiceImpl.INSTANCE;
    }

    @Override
    public ImService getImService() {
        return null;
    }

    @Override
    public LabelService getLabelService() {
        return null;
    }

    @Override
    public LoginService getLoginService() {
        return LoginServiceImpl.INSTANCE;
    }

    @Override
    public MessageService getMessageService() {
        return null;
    }

    @Override
    public PersonalService getPersonalService() {
        return null;
    }

    @Override
    public SnsService getSnsService() {
        return null;
    }
}
