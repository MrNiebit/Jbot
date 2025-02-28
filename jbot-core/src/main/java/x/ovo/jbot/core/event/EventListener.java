package x.ovo.jbot.core.event;

import lombok.Getter;
import lombok.NonNull;
import x.ovo.jbot.core.plugin.Plugin;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 事件监听器
 * <p>{@code E} 是需要监听的事件类型，{@code S} 是需要监听的事件源类型
 *
 * @author ovo created on 2025/02/18.
 */
public abstract class EventListener<E extends Event<S>, S> {

    /** 事件监听器所属的插件 */
    protected final Plugin plugin;
    /** 上下文 */
//    protected final Context ctx;
    /** Event 类 */
    private @Getter Class<? extends E> eventClass;
    /** Source 类 */
    private @Getter Class<? extends S> sourceClass;

    @SuppressWarnings("unchecked")
    public EventListener(Plugin plugin) {
        this.plugin = plugin;
//        this.ctx = plugin.getCtx();
        // 获取泛型参数
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType type) {
            Type[] types = type.getActualTypeArguments();
            if (types[0] instanceof ParameterizedType eventType) {
                this.eventClass = (Class<? extends E>) eventType.getRawType();
            } else if (types[0] instanceof Class<?>) {
                this.eventClass = (Class<? extends E>) types[0];
            }
            if (types[1] instanceof Class<?> sourceType) {
                this.sourceClass = (Class<? extends S>) sourceType;
            }
        }
    }


    /**
     * 判断当前事件监听器是否支持处理特定事件。
     *
     * @param event 需要判断的事件对象，它是一个泛型参数，具体类型由子类决定。
     * @param source 事件源对象，它是一个泛型参数，具体类型由子类决定。
     * @return 如果处理器支持处理该事件，则返回true；否则返回false。
     */
    public abstract boolean support(@NonNull E event, S source);

    /**
     * 处理特定事件的方法。
     *
     * @param event  需要处理的事件对象，它是一个泛型参数，具体类型由子类决定。
     * @param source 事件源对象，它是一个泛型参数，具体类型由子类决定。
     * @return boolean 此事件监听器是否成功匹配到对应事件并且执行了处理逻辑，只有返回值为true（代表这个监听器匹配到对应事件并且执行了处理逻辑）时，executeNext方法才会生效
     */
    public abstract boolean onEvent(@NonNull E event, S source);

    /**
     * 判断是否应该继续执行某些操作。
     *
     * @return 布尔值
     */
    public boolean executeNext() {
        return this.plugin.getConfig().getBoolean("next", true);
    }

}
