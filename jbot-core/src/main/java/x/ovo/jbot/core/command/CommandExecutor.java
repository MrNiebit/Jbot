package x.ovo.jbot.core.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import picocli.CommandLine;
import x.ovo.jbot.core.plugin.Plugin;

/**
 * 命令执行器
 * @author ovo created on 2025/02/18.
 */
@RequiredArgsConstructor
@CommandLine.Command(sortOptions = false, resourceBundle = "i18n", mixinStandardHelpOptions = true)
public abstract class CommandExecutor {

    protected @Setter Command command;
    /** 命令执行器所属的插件 */
    protected @Getter final Plugin plugin;
    /** 命令的相关元数据，由运行时CommandLine注入，在子类实现中可能会用到 */
    protected @CommandLine.Spec CommandLine.Model.CommandSpec spec;

    /**
     * 获取无执行权限提示
     *
     * @return {@link String }
     */
    public String getNoPermissionTip() {
        return null;
    }

    class PluginVersionProvider implements CommandLine.IVersionProvider {
        public PluginVersionProvider() {
        }

        @Override
        public String[] getVersion() {
            return new String[]{plugin.getDescription().getVersion()};
        }
    }

}
