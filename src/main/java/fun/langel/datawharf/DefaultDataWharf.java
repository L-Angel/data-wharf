package fun.langel.datawharf;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/10/5
 */
public class DefaultDataWharf extends DataWharf<WharfData> {

    public DefaultDataWharf(int channelSize, int bufferSize) {
        super(channelSize, bufferSize);
    }

    /**
     * @param key       data key
     * @param data      data object,string.
     * @param needMerge if true, the data will merge based on key, and add spliterator \n.
     */
    public void produce(String key, String data, boolean needMerge) {
        this.produce(new WharfData(key, data, needMerge));
    }

    /**
     * set consumer object class
     * With default, will create 3 threads to consume data.When buffer is empty, with consumer will
     * wait 1000ms to producer produce data.
     *
     * @param consumerKlass consumer class file.
     */
    public void consume(Class<? extends Consumer> consumerKlass) {
        this.consume(consumerKlass, 3, 1000);
    }

}
