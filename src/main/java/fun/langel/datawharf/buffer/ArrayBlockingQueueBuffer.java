package fun.langel.datawharf.buffer;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public class ArrayBlockingQueueBuffer<T> implements QueueBuffer<T> {


    private final ArrayBlockingQueue<T> queue;

    public ArrayBlockingQueueBuffer(int bufferSize) {
        this.queue = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public boolean save(T data) {
        if (this.queue.remainingCapacity() <= 0) {
            throw new IllegalStateException("Save data to queue failure, queue buffer is full. size : " + this.queue.size());
        }
        return this.queue.add(data);
    }

    @Override
    public int obtain(Collection<T> collection) {
        return this.queue.drainTo(collection);
    }

    @Override
    public int getBufferSize() {
        return this.queue.size();
    }

}
