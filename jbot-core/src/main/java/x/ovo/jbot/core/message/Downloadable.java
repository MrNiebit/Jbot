package x.ovo.jbot.core.message;

/**
 * 可下载的
 *
 * @author ovo created on 2025/02/17.
 */
public interface Downloadable {

    String getAesKey();
    String getFileUrl();
    int getFileType();
    int getSize();

}
