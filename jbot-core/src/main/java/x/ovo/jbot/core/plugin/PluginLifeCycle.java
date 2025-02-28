package x.ovo.jbot.core.plugin;

import io.vertx.core.Promise;

/**
 * 插件生命周期
 *
 * @author ovo created on 2025/02/17.
 */
public interface PluginLifeCycle {

    /**
     * 插件加载时
     */
    default void onLoad() throws Exception {}

    /**
     * 插件加载时
     *
     * @param promise promise
     */
    default void onLoad(Promise<Void> promise){
        try {
            this.onLoad();
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
    }

    /**
     * 插件卸载时
     */
    default void onUnload() throws Exception {}

    /**
     * 插件卸载时
     *
     * @param promise promise
     */
    default void onUnload(Promise<Void> promise){
        try {
            this.onUnload();
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
    }

    /**
     * 插件启用时
     */
    default void onEnable() throws Exception {}

    /**
     * 插件启用时
     *
     * @param promise promise
     */
    default void onEnable(Promise<Void> promise){
        try {
            this.onEnable();
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
    }

    /**
     * 插件禁用时
     */
    default void onDisable() throws Exception {}

    /**
     * 插件禁用时
     *
     * @param promise promise
     */
    default void onDisable(Promise<Void> promise){
        try {
            this.onDisable();
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
    }

}
