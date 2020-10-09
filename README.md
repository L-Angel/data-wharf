## Data Wharf

#### 项目说明
 本地缓存队列，参考skywalking data-carrier模块实现

#### 使用介绍

```java

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/10/5
 * <p>
 * Thread : data-wharf-consumer-2
 * Data :
 * Thread : data-wharf-consumer-0
 * Data :
 * Thread : data-wharf-consumer-1
 * Data :
 * key-1 : value-1
 * key-4 : value-4
 * key-7 : value-7
 * key-0 : value-10
 * key-3 : value-13
 * key-6 : value-16
 * key-9 : value-19
 * key-2 : value-22
 * key-5 : value-25
 * key-8 : value-28
 * key-1 : value-31
 * key-4 : value-34
 * key-0 : value-0
 * key-3 : value-3
 * key-6 : value-6
 * key-9 : value-9
 * key-2 : value-12
 * key-5 : value-15
 * key-8 : value-18
 * key-1 : value-21
 * key-2 : value-2
 * key-5 : value-5
 * key-8 : value-8
 * key-1 : value-11
 * key-4 : value-14
 * key-7 : value-17
 * key-0 : value-20
 * key-3 : value-23
 * key-6 : value-26
 * key-9 : value-29
 * key-2 : value-32
 * key-5 : value-35
 * key-4 : value-24
 * key-7 : value-27
 * key-0 : value-30
 * key-3 : value-33
 * Thread : data-wharf-consumer-2
 * Data :
 * Thread : data-wharf-consumer-1
 * Data :
 * key-7 : value-37
 * key-0 : value-40
 * key-3 : value-43
 * key-6 : value-46
 * key-9 : value-49
 * key-2 : value-52
 * key-5 : value-55
 * key-8 : value-58
 * key-8 : value-38
 * key-1 : value-41
 * key-4 : value-44
 * key-7 : value-47
 * key-0 : value-50
 * key-3 : value-53
 * key-6 : value-56
 * key-9 : value-59
 * key-2 : value-62
 * key-5 : value-65
 * key-8 : value-68
 * key-1 : value-71
 * key-4 : value-74
 * key-1 : value-61
 * key-4 : value-64
 * key-7 : value-67
 * key-0 : value-70
 * key-3 : value-73
 * Thread : data-wharf-consumer-0
 * Data :
 * key-6 : value-36
 * key-9 : value-39
 * key-2 : value-42
 * key-5 : value-45
 * key-8 : value-48
 * key-1 : value-51
 * key-4 : value-54
 * key-7 : value-57
 * key-0 : value-60
 * key-3 : value-63
 * key-6 : value-66
 * key-9 : value-69
 * key-2 : value-72
 * key-5 : value-75
 * Thread : data-wharf-consumer-2
 * Data :
 * key-7 : value-77
 * key-0 : value-80
 * key-3 : value-83
 * key-6 : value-86
 * key-9 : value-89
 * key-2 : value-92
 * key-5 : value-95
 * key-8 : value-98
 * Thread : data-wharf-consumer-1
 * Data :
 * key-6 : value-76
 * key-9 : value-79
 * key-2 : value-82
 * key-5 : value-85
 * key-8 : value-88
 * key-1 : value-91
 * key-4 : value-94
 * key-7 : value-97
 * Thread : data-wharf-consumer-0
 * Data :
 * key-8 : value-78
 * key-1 : value-81
 * key-4 : value-84
 * key-7 : value-87
 * key-0 : value-90
 * key-3 : value-93
 * key-6 : value-96
 * key-9 : value-99
 */
public class DataWharfTest {

    @Test
    public void defaultWharfDataTest() throws InterruptedException {
        DefaultDataWharf dataWharf = DataWharf.getDefault();
        dataWharf.consume(DemoConsumer.class, 3, 1000);
        for (int idx = 0; idx < 100; idx++) {
            if (idx % 4 == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            dataWharf.produce("key-" + (idx % 10), "value-" + idx, false);
        }
        TimeUnit.SECONDS.sleep(10);
    }

    public static class DemoConsumer implements Consumer<WharfData> {

        @Override
        public void consume(List<WharfData> data) {
            System.out.println("Thread : " + Thread.currentThread().getName());
            System.out.println("Data : ");
            for (WharfData wd : data) {
                System.out.println(wd.getKey() + " : " + wd.getData());
            }
        }
    }
}
```
## Data Wharf

#### Introduction
 Local async cache queue, reference to module data-carrier in skywalking.
