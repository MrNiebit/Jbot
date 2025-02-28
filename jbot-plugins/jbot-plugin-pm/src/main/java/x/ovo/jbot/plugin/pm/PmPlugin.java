package x.ovo.jbot.plugin.pm;

import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.plugin.Plugin;

public class PmPlugin extends Plugin {
    @Override
    public CommandExecutor getCommandExecutor() {
        return new PmExecutor(this);
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return null;
    }
}
