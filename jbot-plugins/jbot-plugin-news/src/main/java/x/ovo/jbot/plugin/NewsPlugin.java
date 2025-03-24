package x.ovo.jbot.plugin;

import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.plugin.Plugin;

public class NewsPlugin extends Plugin {
    @Override
    public CommandExecutor getCommandExecutor() {
        return null;
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new NewsListener(this);
    }
}
