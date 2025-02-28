package x.ovo.jbot.impl.contact;

import x.ovo.jbot.core.contact.Contactable;
import x.ovo.jbot.core.contact.RetrievalStrategy;
import x.ovo.jbot.core.contact.RetrievalType;

import java.util.Iterator;
import java.util.Objects;

/**
 * 根据昵称进行检索的策略
 *
 * @author ovo created on 2025/02/18.
 */
public class NickNameStrategy extends RetrievalStrategy {
    @Override
    public Contactable get(Iterator<? extends Contactable> iterator, String key) {
        while (iterator.hasNext()) {
            Contactable contactable = iterator.next();
            if (Objects.equals(contactable.getNickname(), key)) return contactable;
        }
        return null;
    }

    @Override
    public boolean remove(Iterator<? extends Contactable> iterator, String key) {
        while (iterator.hasNext()) {
            if (Objects.equals(iterator.next().getNickname(), key)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void init() {
        MAP.put(RetrievalType.NICK_NAME, this);
    }
}
