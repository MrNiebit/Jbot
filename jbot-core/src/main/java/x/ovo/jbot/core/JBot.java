package x.ovo.jbot.core;


import io.vertx.core.Future;

/**
 * JBot
 *
 * @author ovo created on 2025/02/19.
 */
public interface JBot {

    Future<Void> start();

    Future<Void> stop();

    Future<Void> initManager();

    Future<Void> mkdir();
}
