package fun.langel.datawharf;

import fun.langel.datawharf.drive.ConsumerDrive;
import fun.langel.datawharf.partition.SimpleRollingPartitioner;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public class DataWharf<D> {

    public static DefaultDataWharf defaultInstance;

    private static final int DEFAULT_BUFFER_CHANNEL_SIZE = 3;

    private static final int DEFAULT_BUFFER_SIZE = 10000;

    private static final int TRACKER_BUFFER_SIZE = 10000;

    private final Channels<D> channels;

    private volatile ConsumerDrive consumerDrive;

    private AtomicBoolean working = new AtomicBoolean(false);

    private static final Object LOCK = new Object();

    private static final Object TRACKER_LOCK = new Object();

    /**
     * @param channelSize channel number
     * @param bufferSize  which channel has one buffer, that set buffer size of every channel.
     */
    public DataWharf(int channelSize, int bufferSize) {
        this.channels = new Channels(channelSize, bufferSize, new SimpleRollingPartitioner<>());
    }

    /**
     * produce data
     *
     * @param data data object
     * @return
     */
    public boolean produce(D data) {
        if (data == null) {
            return false;
        }
        if (working.get()) {
            this.channels.store(data);
            return false;
        }
        return true;
    }

    /**
     * @param consumerKlass
     * @param size          the number of thread will start.
     * @param consumeIdle   ms,consumer thread pause time.
     */
    public void consume(Class<? extends Consumer> consumerKlass, int size, int consumeIdle) {
        if (this.consumerDrive == null) {
            this.consumerDrive = new ConsumerDrive(consumerKlass, size, consumeIdle, this.channels);
            this.consumerDrive.begin();
            this.working.set(true);
            return;
        }
        if (!this.consumerDrive.running()) {
            this.consumerDrive.begin();
            this.working.set(true);
            return;
        }
        this.working.set(false);
    }

    /**
     * finish data wharf
     */
    public void finish() {
        if (this.consumerDrive != null && this.consumerDrive.running()) {
            this.consumerDrive.finish();
        }
        this.working.set(false);
        this.consumerDrive = null;
    }

    public static DefaultDataWharf getDefault() {
        if (defaultInstance == null) {
            synchronized (LOCK) {
                if (defaultInstance == null) {
                    defaultInstance = new DefaultDataWharf(DEFAULT_BUFFER_CHANNEL_SIZE, DEFAULT_BUFFER_SIZE);
                }
            }
        }
        return defaultInstance;
    }
}
