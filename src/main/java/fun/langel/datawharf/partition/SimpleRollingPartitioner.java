package fun.langel.datawharf.partition;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/24
 */
public class SimpleRollingPartitioner<D> implements DataPartitioner<D> {

    private volatile int count = 0;

    @Override
    public int partition(int total, D data) {
        return Math.abs(count++ % total);
    }
}
