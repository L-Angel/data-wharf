package fun.langel.datawharf.drive;


import fun.langel.datawharf.Channels;
import fun.langel.datawharf.Consumer;
import fun.langel.datawharf.ConsumerThread;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/24
 */
public class ConsumerDrive implements Drive {

    private ConsumerThread[] consumerThreads;

    private Class<? extends Consumer> consumerKlass;

    private int consumeThreadNum = 0;

    private int consumeIdle = 10;

    private AtomicBoolean running = new AtomicBoolean(false);

    private Channels channels;

    public ConsumerDrive(Class<? extends Consumer> consumeKlass,
                         int consumeThreadNum,
                         int consumeIdle,
                         Channels channels) {
        this.consumeThreadNum = consumeThreadNum;
        this.consumerKlass = consumeKlass;
        this.consumeIdle = consumeIdle;
        this.channels = channels;
    }

    @Override
    public boolean running() {
        return running.get();
    }

    @Override
    public void begin() {
        this.createConsumeThreadsAndAllocateBuffer();
        this.running.set(true);
    }

    @Override
    public void finish() {
        this.shutdownConsumeThreads();
        this.running.set(false);
    }

    private void createConsumeThreadsAndAllocateBuffer() {
        this.consumerThreads = new ConsumerThread[this.consumeThreadNum];
        for (int idx = 0, len = this.consumerThreads.length; idx < len; idx++) {
            try {
                Constructor<Consumer> constructor = (Constructor<Consumer>) this.consumerKlass.getConstructor();
                constructor.setAccessible(true);
                Consumer consumer = constructor.newInstance();
                ConsumerThread ct = new ConsumerThread("snake-datawharf-consumer-" + idx, consumer, consumeIdle);
                ct.addDataSource(this.channels.getQueueBuffer(idx % this.channels.getChannelSize()));
                ct.start();
                this.consumerThreads[idx] = ct;
            } catch (Throwable ex) {

            }
        }
    }

    private void shutdownConsumeThreads() {
        if (running.get()) {
            for (ConsumerThread thread : this.consumerThreads) {
                thread.shutdown();
                thread.interrupt();
            }
            this.consumerThreads = null;
        }
    }
}
