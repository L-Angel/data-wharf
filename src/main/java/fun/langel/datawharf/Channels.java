package fun.langel.datawharf;


import fun.langel.datawharf.buffer.ArrayBlockingQueueBuffer;
import fun.langel.datawharf.buffer.QueueBuffer;
import fun.langel.datawharf.partition.DataPartitioner;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public class Channels<T> {

    private QueueBuffer<T>[] bufferChannels;

    private DataPartitioner<T> dataPartitioner;

    public Channels(int channelSize, int bufferSize, DataPartitioner<T> dataPartitioner) {
        this.bufferChannels = new QueueBuffer[channelSize];
        for (int idx = 0, len = this.bufferChannels.length; idx < len; idx++) {
            this.bufferChannels[idx] = new ArrayBlockingQueueBuffer(bufferSize);
        }
        this.dataPartitioner = dataPartitioner;
    }

    public int getChannelSize() {
        return this.bufferChannels.length;
    }

    public void store(T data) {
        int channel = this.dataPartitioner.partition(this.bufferChannels.length, data);
        QueueBuffer buffer = this.bufferChannels[channel];
        buffer.save(data);
    }

    public QueueBuffer<T> getQueueBuffer(int channel) {
        return this.bufferChannels[channel];
    }
}
