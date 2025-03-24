package x.ovo.jbot.plugin.memory;

import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.plugin.Plugin;

public class MemoryPlugin extends Plugin {
    @Override
    public CommandExecutor getCommandExecutor() {
        return new MemoryExecutor(this);
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return null;
    }
}
