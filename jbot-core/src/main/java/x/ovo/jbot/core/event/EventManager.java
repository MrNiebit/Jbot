package x.ovo.jbot.core.event;

import io.vertx.core.Future;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.manager.Manager;
import x.ovo.jbot.core.manager.ManagerLifeCycle;
import x.ovo.jbot.core.plugin.Plugin;

/**
 * 事件管理器
 *
 * @author ovo created on 2025/02/17.
 */
public interface EventManager extends Manager, ManagerLifeCycle {

    /**
     * 注册插件中的事件监听器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> register(Plugin plugin);

    /**
     * 卸载插件中的事件监听器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unregister(Plugin plugin);

    /**
     * 发布事件
     *
     * @param event 事件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> publish(Event<?> event);

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setEventManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setEventManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
