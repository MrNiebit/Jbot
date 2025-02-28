package x.ovo.jbot.core.plugin;

import io.vertx.core.Future;
import x.ovo.jbot.core.Context;
import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.manager.Manager;
import x.ovo.jbot.core.manager.ManagerLifeCycle;

import java.io.File;
import java.util.Collection;

/**
 * 插件管理器
 *
 * @author ovo created on 2025/02/17.
 */
public interface PluginManager extends Manager, ManagerLifeCycle {

    void reInit();

    /**
     * 加载插件目录下的所有插件，存放在内部容器中
     */
    void loadPlugins();

    /**
     * 获取插件
     *
     * @param name 插件名称
     * @return {@link Plugin }
     */
    Plugin get(String name);

    /**
     * 获取配置
     *
     * @param name 插件名称
     * @return {@link PluginConfig }
     */
    PluginConfig getConfig(String name);

    /**
     * 根据名称加载插件
     *
     * @param name 名字
     * @return {@link Future }<{@link Plugin }>
     */
    Future<Plugin> load(String name);

    /**
     * 根据文件加载插件
     *
     * @param file 文件
     * @return {@link Future }<{@link Plugin }>
     */
    Future<Plugin> load(File file);

    /**
     * 卸载插件
     *
     * @param name 名字
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unload(String name);

    /**
     * 卸载插件
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unload(Plugin plugin);

    /**
     * 启用插件
     *
     * @param name   名字
     * @param target 目标
     * @return {@link Future }<{@link Void }>
     */
    Future<String> enable(String name, Contactable target);

    /**
     * 禁用插件
     *
     * @param name   名字
     * @param target 目标
     * @return {@link Future }<{@link Void }>
     */
    Future<String> disable(String name, Contactable target);

    /**
     * 获取所有已加载的插件列表
     *
     * @return {@link Collection }<{@link Plugin }>
     */
    Collection<Plugin> list();

    /**
     * 获取指定来目标的所有可用的已加载的插件
     *
     * @param target 目标
     * @return {@link Collection }<{@link Plugin }>
     */
    Collection<Plugin> availableList(Contactable target);

    /**
     * 获取限制模式
     *
     * @return {@link LimitMode }
     */
    LimitMode getLimitMode();

    /**
     * 检查消息来源是否在插件限制名单内
     *
     * @param plugin 插件
     * @param target 目标
     * @return boolean
     */
    boolean isLimited(Plugin plugin, Contactable target);

    /**
     * 保存配置
     *
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> saveConfig();

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setPluginManager(this)).await();
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setPluginManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
