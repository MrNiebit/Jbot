package x.ovo.jbot.core.adapter;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import x.ovo.jbot.core.common.exception.AdapterException;
import x.ovo.jbot.core.service.*;

/**
 * 适配器
 *
 * @author ovo created on 2025/02/23.
 */
public interface Adapter {

    String name();

    Future<Void> start();

    ContactService getContactService();

    DownloadService getDownloadService();

    FavoriteService getFavoriteService();

    GroupService getGroupService();

    ImService getImService();

    LabelService getLabelService();

    LoginService getLoginService();

    MessageService getMessageService();

    PersonalService getPersonalService();

    SnsService getSnsService();


    Future<Void> onInit(JsonObject config);

    Future<Void> onDestroy();

    default void init(JsonObject config) {
        this.onInit(config).onFailure(e -> {throw new AdapterException("适配器初始化失败", e);}).await();
    }

    default void destroy() {
        this.onDestroy().onFailure(e -> {throw new AdapterException("适配器关闭失败", e);}).await();
    }

}
