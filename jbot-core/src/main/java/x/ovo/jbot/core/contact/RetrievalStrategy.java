package x.ovo.jbot.core.contact;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 联系人检索策略
 *
 * @author ovo created on 2025/02/18.
 */
public abstract class RetrievalStrategy {

    protected static final Map<RetrievalType, RetrievalStrategy> MAP = new HashMap<>();

    public static RetrievalStrategy of(RetrievalType type) {
        return MAP.get(type);
    }

    /**
     * 根据键值从迭代器中获取联系人。
     *
     * @param iterator 联系人的迭代器，用于遍历联系人集合。
     * @param key      要检索的联系人的键值。
     * @return 返回与键值匹配的 {@link Contactable } ，如果没有找到，则返回null。
     */
    public abstract Contactable get(Iterator<? extends Contactable> iterator, String key);

    /**
     * 从迭代器中移除与键值匹配的联系人。
     *
     * @param iterator 联系人的迭代器，用于遍历并移除联系人。
     * @param key      要移除的联系人的键值。
     * @return 如果成功移除了匹配的联系人，则返回true；否则返回false。
     */
    public abstract boolean remove(Iterator<? extends Contactable> iterator, String key);

    /**
     * 初始化，将自己注册到检索策略的映射表中。
     */
    public abstract void init();
}
