package fun.langel.datawharf.buffer;

import java.util.Collection;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/23
 */
public interface QueueBuffer<T> {

    boolean save(T data);

    int obtain(Collection<T> collection);

    int getBufferSize();
}
