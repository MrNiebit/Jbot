package x.ovo.jbot.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.core.Context;

/**
 * 事件
 *
 * @author ovo created on 2025/02/17.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class Event<T> {

    protected final T data;

    public void publish() {
        Context.get().getEventManager().publish(this)
                .onFailure(e -> log.error("[{}] 事件发布失败：{}", this.getClass().getSimpleName(), data, e))
                .onSuccess(v -> log.debug("[{}] 事件发布成功：{}", this.getClass().getSimpleName(), data));
    }
}
