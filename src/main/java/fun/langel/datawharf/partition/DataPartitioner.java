package fun.langel.datawharf.partition;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/24
 */
public interface DataPartitioner<D> {

    int partition(int total, D data);

}
