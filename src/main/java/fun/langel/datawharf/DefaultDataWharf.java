package fun.langel.datawharf;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/10/5
 */
public class DefaultDataWharf extends DataWharf<WharfData> {

    public DefaultDataWharf(int channelSize, int bufferSize) {
        super(channelSize, bufferSize);
    }

    public void produce(String key, String data, boolean needMerge) {
        this.produce(new WharfData(key, data, needMerge));
    }

}
