package x.ovo.jbot.core.manager;

import io.vertx.core.Future;

/**
 * 管理器接口
 *
 * @author ovo created on 2025/02/17.
 */
public interface Manager {

    Future<Void> init();

    Future<Void> close();

}
