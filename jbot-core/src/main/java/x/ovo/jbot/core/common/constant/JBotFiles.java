package x.ovo.jbot.core.common.constant;

import java.io.File;

/**
 * jbot 文件常量类
 *
 * @author ovo created on 2025/02/18.
 */
public final class JBotFiles {

    public static final File DATA_DIR = new File(System.getProperty("user.dir"));
    public static final File ADAPTER_DIR = new File(DATA_DIR, "adapter");
    public static final File LOG_DIR = new File(DATA_DIR, "log");
    public static final File CONFIG_DIR = new File(DATA_DIR, "config");
    public static final File PLUGIN_DIR = new File(DATA_DIR, "plugin");
    public static final File IMAGE_DIR = new File(DATA_DIR, "image");
    public static final File VOICE_DIR = new File(DATA_DIR, "voice");
    public static final File VIDEO_DIR = new File(DATA_DIR, "video");

    public static final File DEVICE_FILE = new File(CONFIG_DIR, "device");
    public static final File CONFIG_FILE = new File(CONFIG_DIR, "jbot.json5");
    public static final File LOGIN_FILE = new File(CONFIG_DIR, "memory.card");
    public static final File PLUGIN_CONFIG_FILE = new File(CONFIG_DIR, "plugin.json5");

}
