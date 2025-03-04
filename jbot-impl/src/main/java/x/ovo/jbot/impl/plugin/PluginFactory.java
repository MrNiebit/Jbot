package x.ovo.jbot.impl.plugin;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.io.FileSystem;
import org.graalvm.polyglot.io.IOAccess;
import org.slf4j.LoggerFactory;
import x.ovo.jbot.core.command.CommandExecutor;
import x.ovo.jbot.core.event.CallListener;
import x.ovo.jbot.core.event.EventListener;
import x.ovo.jbot.core.message.entity.*;
import x.ovo.jbot.core.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 插件工厂
 *
 * @author ovo created on 2025/02/18.
 */
public class PluginFactory {


    public static Plugin load(ClassLoader classLoader, String className) throws IOException {
        return className.endsWith("js") ? loadJsPlugin(classLoader, className) : loadJavaPlugin(classLoader, className);
    }

    @SneakyThrows
    private static Plugin loadJavaPlugin(ClassLoader classLoader, String javaFile) {
        return (Plugin) classLoader.loadClass(javaFile).getDeclaredConstructor().newInstance();
    }

    private static Plugin loadJsPlugin(ClassLoader classLoader, String jsFile) throws IOException {
        // 1、通过classLoader加载js文件
        @Cleanup var is = Optional.ofNullable(classLoader.getResourceAsStream(jsFile)).orElseThrow();
        var script = new String(is.readAllBytes());

        // 2、创建多语言运行时环境
        var context = Context.newBuilder()
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .allowIO(IOAccess.newBuilder().fileSystem(new JsPluginFileSystem(classLoader)).build())
                .build();

        // 3、将对象注入多语言运行时环境
        injectClass(jsFile, context);

        // 4、执行js文件获取plugin对象
        var res = context.eval(Source.newBuilder("js", script, "plugin.mjs").build());
        var plugin = res.as(Plugin.class);
        plugin.setRuntime(context);
        return plugin;
    }

    private static void injectClass(String jsFile, Context context) {
        var bindings = context.getBindings("js");
        bindings.putMember("log", LoggerFactory.getLogger(jsFile));
        bindings.putMember("vertx", x.ovo.jbot.core.Context.vertx);
        bindings.putMember("PluginClass", Plugin.class);
        bindings.putMember("EventListenerClass", EventListener.class);
        bindings.putMember("CallListenerClass", CallListener.class);
        bindings.putMember("CommandExecutorClass", CommandExecutor.class);

        bindings.putMember("MessageClass", Message.class);
        bindings.putMember("TextMessageClass", TextMessage.class);
        bindings.putMember("ImageMessageClass", ImageMessage.class);
        bindings.putMember("VideoMessageClass", VideoMessage.class);
        bindings.putMember("VoiceMessageClass", VoiceMessage.class);
        bindings.putMember("BusinessesCardMessageClass", BusinessesCardMessage.class);
        bindings.putMember("PersonalCardMessageClass", PersonalCardMessage.class);
        bindings.putMember("EmoteMessageClass", EmoteMessage.class);
        bindings.putMember("StatusNotifyMessageClass", StatusNotifyMessage.class);
        bindings.putMember("SystemMessageClass", SystemMessage.class);
        bindings.putMember("VerifyMessageClass", VerifyMessage.class);
    }


    /**
     * js 插件文件系统
     *
     * @author ovo created on 2024/12/25.
     * @apiNote 用于js在运行时读取文件进行import
     */
    private record JsPluginFileSystem(ClassLoader classLoader) implements FileSystem {

        @Override
        public Path parsePath(URI uri) {
            return Paths.get(uri);
        }

        @Override
        public Path parsePath(String path) {
            return Paths.get(path);
        }

        @Override
        public void checkAccess(Path path, Set<? extends AccessMode> modes, LinkOption... linkOptions) throws IOException {

        }

        @Override
        public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {

        }

        @Override
        public void delete(Path path) throws IOException {

        }

        @Override
        public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
            @Cleanup InputStream stream = Optional.ofNullable(this.classLoader.getResourceAsStream(path.toString())).orElseThrow();
            return new ReadOnlySeekableByteArrayChannel(stream.readAllBytes());
        }

        @Override
        public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
            return null;
        }

        @Override
        public Path toAbsolutePath(Path path) {
            return path.toAbsolutePath();
        }

        @Override
        public Path toRealPath(Path path, LinkOption... linkOptions) throws IOException {
            return path;
        }

        @Override
        public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
            return Map.of();
        }
    }

}
