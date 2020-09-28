package fun.langel.datawharf.drive;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/24
 */
public interface Drive {

    boolean running();

    void begin();

    void finish();
}