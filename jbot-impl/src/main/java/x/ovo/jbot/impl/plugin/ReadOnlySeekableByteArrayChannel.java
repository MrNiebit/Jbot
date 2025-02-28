package x.ovo.jbot.impl.plugin;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;

/**
 * 只读可寻字节数组通道
 *
 * @author ovo created on 2024/12/25.
 */
@RequiredArgsConstructor
public class ReadOnlySeekableByteArrayChannel implements SeekableByteChannel {

    private final byte[] data;
    private int position;
    private boolean closed;

    @Override
    public int read(ByteBuffer dst) throws IOException {
        if (this.closed) {
            throw new ClosedChannelException();
        }
        int remaining = (int) size() - position;
        if (remaining <= 0) {
            return -1;
        }
        int readBytes = dst.remaining();
        if (readBytes > remaining) {
            readBytes = remaining;
        }
        dst.put(data, position, readBytes);
        position += readBytes;
        return readBytes;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long position() throws IOException {
        return this.position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        if (this.closed) {
            throw new ClosedChannelException();
        }
        position = (int) Math.max(0, Math.min(newPosition, size()));
        return this;
    }

    @Override
    public long size() throws IOException {
        return this.data.length;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        return !this.closed;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }
}
