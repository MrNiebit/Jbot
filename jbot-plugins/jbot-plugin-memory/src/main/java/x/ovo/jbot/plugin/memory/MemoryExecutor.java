package x.ovo.jbot.plugin.memory;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.io.file.FileUtil;
import picocli.CommandLine;
import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.plugin.Plugin;

import java.util.concurrent.Callable;

@Slf4j
@CommandLine.Command(name = "memory", aliases = {"mem"}, description = "查看内存使用情况")
public class MemoryExecutor extends CommandExecutor implements Callable<String> {

    @CommandLine.Option(names = {"-s", "--show"}, defaultValue = "false", description = "显示内存使用情况")
    private boolean show;

    @CommandLine.Option(names = {"-c", "--clear"}, defaultValue = "false", description = "清理内存")
    private boolean clear;

    public MemoryExecutor(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String call() throws Exception {
        if (show) {
            String memory = FileUtil.readableFileSize(Runtime.getRuntime().totalMemory());
            log.info("当前内存占用：{}", memory);
            return "当前内存占用：" + memory;
        }
        if (clear) {
            String before = FileUtil.readableFileSize(Runtime.getRuntime().totalMemory());
            System.gc();
            String after = FileUtil.readableFileSize(Runtime.getRuntime().totalMemory());
            log.info("GC执行成功，执行前内存占用：{}，执行后内存占用：{}", before, after);
            return String.format("GC执行成功，执行前内存占用：%s，执行后内存占用：%s", before, after);
        }
        return "";
    }

}
