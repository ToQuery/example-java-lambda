package io.github.toquery.example.lambda;

import com.google.common.collect.Lists;
import io.github.toquery.example.lambda.core.parallel.BookBlocker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 自定义 ForkJoinPool 提高并行度，但是在这种情况下对于纯计算的任务由于线程切换也会导致cpu效率下降。
 * <a href="https://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream">Java 8 并行流中的自定义线程池</a>
 * <a href="https://stackoverflow.com/questions/52287717/how-to-specify-forkjoinpool-for-java-8-parallel-stream">如何为 Java 8 并行流指定 ForkJoinPool</a>
 */
@Slf4j
public class ParallelStreamTest {

    /**
     * 设置并行度的方式提高处理速度
     * 有两种方式：
     * 1. 代码中指定 System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
     * 2. 启动参数指定 -Djava.util.concurrent.ForkJoinPool.common.parallelism=16
     * <p></p>
     * 在8C的场景下，耗时6s，也就是大约3次循环
     */
    @Test
    public void stream1() throws Exception {
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        List<Integer> testList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        long start = System.currentTimeMillis();
        List<Integer> result = testList.parallelStream().map(item -> {
            try {
                // read from database
                System.out.println("task" + item + ":" + Thread.currentThread());
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item * 10;
        }).collect(Collectors.toList());
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 依据上面的优化，将并行流 parallelStream 放入 ForkJoinPool 中
     * 耗时2s左右
     */
    @Test
    public void stream2() throws Exception {
        ForkJoinPool pool = new ForkJoinPool(20);
        List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        long start = System.currentTimeMillis();
        List<Integer> result = pool.submit(() -> testList.parallelStream().map(item -> {
            try {
                // read from database
                System.out.println("task" + item + ":" + Thread.currentThread());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return item * 10;
        }).collect(Collectors.toList())).join();
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 第二次优化，将流声明创建放入外部，ForkJoinPool submit 只处理 Collectors 业务
     * 耗时与上面相同，2s左右
     */
    @Test
    public void stream3() throws Exception {
        ForkJoinPool pool = new ForkJoinPool(20);
        List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        long start = System.currentTimeMillis();
        Stream<Integer> stream = testList.parallelStream().map(item -> {
            try {
                // read from database
                System.out.println("task" + item + ":" + Thread.currentThread());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return item * 10;
        });
        List<Integer> result = pool.submit(() -> stream.collect(Collectors.toList())).join();
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void stream4() throws Exception {
        ForkJoinPool.managedBlock(new BookBlocker());
        ForkJoinPool pool = new ForkJoinPool(20);
        List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        long start = System.currentTimeMillis();
        Stream<Integer> stream = testList.parallelStream().map(item -> {
            try {
                // read from database
                System.out.println("task" + item + ":" + Thread.currentThread());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return item * 10;
        });
        List<Integer> result = pool.submit(() -> stream.collect(Collectors.toList())).join();
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }


    /**
     * ManagedBlocker
     * <a href="https://blog.csdn.net/heng_zou/article/details/118193846">...</a>
     * <a href="https://stackoverflow.com/questions/59466375/size-of-thread-in-parallelstream-greater-than-cpu-cores">...</a>
     */
    @Test
    public void testManagedBlocker() throws InterruptedException {
        // wait to be able to connect with VisualVM
         Thread.sleep(10_000);

        int s = IntStream.range(0, 100)
                .parallel()
                .peek(number -> {
                    doWork();

                    // Run a managed blocker some times.
                    // Every time it blocks, a new worker thread might be started.
                    if (ThreadLocalRandom.current().nextInt(10) == 0) {
                        try {
                            ForkJoinPool.managedBlock(new ForkJoinPool.ManagedBlocker() {
                                @Override
                                public boolean block() throws InterruptedException {
                                    Thread.sleep(1_000);
                                    return true;
                                }

                                @Override
                                public boolean isReleasable() {
                                    return false;
                                }
                            });
                        } catch (InterruptedException ignored) {
                        }
                    }
                })
                .sum();
    }

    /**
     * Some CPU bound workload
     **/
    void doWork() {
        for (int i = 0; i < 1_000_000; i++) {
            Math.random();
        }
    }


}
