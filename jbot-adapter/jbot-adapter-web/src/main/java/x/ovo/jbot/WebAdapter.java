package x.ovo.jbot;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.adapter.Adapter;
import x.ovo.jbot.core.service.*;



@Slf4j
public class WebAdapter implements Adapter {
    @Override
    public String name() {
        return "web adapter";
    }

    @Override
    public void setConfig(JsonObject config) {

    }

    @Override
    public void init() {
        log.info("web adapter init");
    }

    @Override
    public Future<Void> start() {
        return Future.succeededFuture();
    }

    @Override
    public ContactService getContactService() {
        return null;
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
        return null;
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
        return null;
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
