package fun.langel.datawharf;

import fun.langel.datawharf.buffer.QueueBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/24
 */
public class ConsumerThread extends Thread {

    private Consumer consumer;

    private List<DataSource> datasources;

    private int consumeIdle = 10;

    private volatile boolean running = false;

    public ConsumerThread(String threadName, Consumer consumer, int consumeIdle) {
        super(threadName);
        this.consumer = consumer;
        this.datasources = new LinkedList<>();
        this.consumeIdle = consumeIdle;
    }

    public void addDataSource(QueueBuffer queueBuffer) {
        this.datasources.add(new DataSource(queueBuffer));
    }

    @Override
    public void run() {
        running = true;
        final List consumeDatas = new ArrayList(1000);
        while (running) {
            if (!consume(consumeDatas)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(this.consumeIdle);
                } catch (Throwable ex) {
                    // ignore
                }
            }
        }
        consume(consumeDatas);

    }


    private boolean consume(List consumeDatas) {
        for (DataSource datasource : this.datasources) {
            datasource.obtain(consumeDatas);
        }
        if (!consumeDatas.isEmpty()) {
            try {
                this.consumer.consume(consumeDatas);
                return true;
            } finally {
                consumeDatas.clear();
            }
        }
        return false;
    }

    public void shutdown() {
        this.running = false;
    }

    static class DataSource {

        private QueueBuffer queueBuffer;

        public DataSource(QueueBuffer queueBuffer) {
            this.queueBuffer = queueBuffer;
        }

        public int obtain(List data) {
            return this.queueBuffer.obtain(data);
        }
    }
}
