package fun.langel.datawharf;

import java.util.List;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public interface Consumer<T> {

    void consume(List<T> data);

}
