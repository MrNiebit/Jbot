package x.ovo.jbot.core.manager;

import io.vertx.core.Future;

/**
 * 管理器生命周期
 *
 * @author ovo created on 2025/02/17.
 */
public interface ManagerLifeCycle {

    default Future<Void> onInit() throws Exception {
        return Future.succeededFuture();
    }

    default Future<Void> onDestroy() throws Exception {
        return Future.succeededFuture();
    }
}
