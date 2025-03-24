package x.ovo.jbot.adapter.apad;

import io.vertx.core.Future;
import lombok.Cleanup;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IoUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@UtilityClass
public class VideoUtil {

    @Data
    public static class VideoInfo {
        private long duration;
        private String frame;
        private String video;
    }

    public static Future<VideoInfo> parse(String base64) {
        return Future.future(promise -> {
            var info = new VideoInfo();
            info.video = base64;
            try {
                @Cleanup var is = IoUtil.toStream(Base64.decode(base64));
                @Cleanup var grabber = new FFmpegFrameGrabber(is);
                grabber.start();
                info.duration = TimeUnit.MICROSECONDS.toSeconds(grabber.getLengthInTime());
                grabber.setTimestamp(TimeUnit.SECONDS.toMicros(1));
                // 抓取第一帧
                Frame frame = grabber.grabImage();
                if (frame != null) {
                    // 转换为BufferedImage
                    @Cleanup var converter = new Java2DFrameConverter();
                    BufferedImage image = converter.getBufferedImage(frame);
                    @Cleanup var os = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", os);
                    info.setFrame(Base64.encode(os.toByteArray()));
                    promise.complete(info);
                } else {
                    promise.fail("无法获取视频帧");
                }
                grabber.stop();
                grabber.release();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }


}
