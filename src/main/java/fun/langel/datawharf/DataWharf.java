package fun.langel.datawharf;

import fun.langel.datawharf.drive.ConsumerDrive;
import fun.langel.datawharf.partition.SimpleRollingPartitioner;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public class DataWharf<D> {

    public static DataWharf defaultInstance;

    public static DataWharf trackerInstance;

    private static final int DEFAULT_BUFFER_CHANNEL_SIZE = 3;

    private static final int DEFAULT_BUFFER_SIZE = 10000;

    private static final int TRACKER_BUFFER_SIZE = 10000;

    private final Channels<D> channels;

    private volatile ConsumerDrive consumerDrive;

    private AtomicBoolean working = new AtomicBoolean(false);

    private static final Object LOCK = new Object();

    private static final Object TRACKER_LOCK = new Object();

    public DataWharf(int channelSize, int bufferSize) {
        this.channels = new Channels(channelSize, bufferSize, new SimpleRollingPartitioner<>());
    }

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

    public boolean produce(D data, boolean needMerge) {
        if (data instanceof WharfData) {
            WharfData wd = (WharfData) data;
            wd.setNeedMerge(needMerge);
        }
        return produce(data);
    }

    /**
     * @param consumerKlass
     * @param size          启动的线程数
     * @param consumeIdle   ms,暂停时间
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

    public void finish() {
        if (this.consumerDrive != null && this.consumerDrive.running()) {
            this.consumerDrive.finish();
        }
        this.working.set(false);
        this.consumerDrive = null;
    }

    public static DataWharf getDefault() {
        if (defaultInstance == null) {
            synchronized (LOCK) {
                if (defaultInstance == null) {
                    defaultInstance = new DataWharf<>(DEFAULT_BUFFER_CHANNEL_SIZE, DEFAULT_BUFFER_SIZE);
                }
            }
        }
        return defaultInstance;
    }
}
