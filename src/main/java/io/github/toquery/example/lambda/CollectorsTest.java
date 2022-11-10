package io.github.toquery.example.lambda;

import io.github.toquery.example.lambda.collectors.BookCollectors;
import io.github.toquery.example.lambda.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class CollectorsTest extends BaseTest {


    /**
     * 输出初始化数据
     */
    @BeforeAll
    public static void beforeAll() {
        log.info("{}原始数据{}", separation, separation);
        CollectorsTest.printLog(BOOKS);
    }

    @Test
    public void testToList() {
        List<String> list1 = BOOKS.stream().map(Book::getName).collect(Collectors.toList());
        log.info("{}testToList{}", separation, separation);
        CollectorsTest.printLog(list1);

        // jdk9+
        List<String> list2 = BOOKS.stream().map(Book::getName).toList();
        log.info("{}testToList{}", separation, separation);
        CollectorsTest.printLog(list2);
    }

    @Test
    public void testToCollection() {
        Set<String> set = BOOKS.stream().map(Book::getName).collect(Collectors.toCollection(TreeSet::new));
        log.info("{}testToCollection{}", separation, separation);
        CollectorsTest.printLog(set);

        List<String> list = BOOKS.stream().map(Book::getName).collect(Collectors.toCollection(ArrayList::new));
        log.info("{}testToCollection{}", separation, separation);
        CollectorsTest.printLog(set);
    }

    @Test
    public void testToMap() {
        Map<String, Book> bookNameMap = BOOKS.stream().collect(Collectors.toMap(Book::getName, Function.identity()));
        log.info("{}testToMap{}", separation, separation);
        CollectorsTest.printLog(bookNameMap);


        Map<String, Double> bookComputeValueMap = BOOKS.stream().collect(Collectors.toMap(Book::getName, BookCollectors::computeValue));
        log.info("{}testToMap{}", separation, separation);
        CollectorsTest.printLog(bookComputeValueMap);

        // 将同一个出版社合并，并用，拼接
        Map<String, String> pressBookName = BOOKS.stream().collect(Collectors.toMap(Book::getPress, Book::getName, (s, a) -> s + ", " + a));
        log.info("{}testToMap{}", separation, separation);
        CollectorsTest.printLog(pressBookName);

        // TODO mapFactory
//        Map<String, String> pressBookName = BOOKS.stream().collect(Collectors.toMap(Book::getPress, Book::getName, (s, a) -> s + ", " + a));
//        log.info("{}testToMap{}", separation, separation);
//        CollectorsTest.printLog(pressBookName);
    }

    @Test
    public void testMinByOrMaxBy() {
        Optional<Book> minOptionalBook1 = BOOKS.stream().min(Comparator.comparing(Book::getQuantity));
        log.info("{}testMinByOrMaxBy{}", separation, separation);
        log.info("{}", minOptionalBook1.get());


        Optional<Book> minOptionalBook2 = BOOKS.stream().collect(Collectors.minBy(Comparator.comparing(Book::getQuantity)));
        log.info("{}testMinByOrMaxBy{}", separation, separation);
        log.info("{}", minOptionalBook2.get());


        Optional<Book> maxOptionalBook1 = BOOKS.stream().max(Comparator.comparing(Book::getQuantity));
        log.info("{}testMinByOrMaxBy{}", separation, separation);
        log.info("{}", maxOptionalBook1.get());

        Optional<Book> maxOptionalBook2 = BOOKS.stream().collect(Collectors.maxBy(Comparator.comparing(Book::getQuantity)));
        log.info("{}testMinByOrMaxBy{}", separation, separation);
        log.info("{}", maxOptionalBook2.get());
    }


    @Test
    public void testJoining() {
        String joined1 = BOOKS.stream().map(Book::getName).collect(Collectors.joining());
        log.info("{}testJoining{}", separation, separation);
        log.info(joined1);

        String joined2 = BOOKS.stream().map(Book::getName).collect(Collectors.joining(";"));
        log.info("{}testJoining{}", separation, separation);
        log.info(joined2);

        String joined3 = BOOKS.stream().map(Book::getName).collect(Collectors.joining(";", "#prefix#", "#suffix#"));
        log.info("{}testJoining{}", separation, separation);
        log.info(joined3);
    }

    @Test
    public void testSumming() {
        int total1 = BOOKS.stream().collect(Collectors.summingInt(Book::getQuantity));
        log.info("{}testSumming{}", separation, separation);
        log.info("{}", total1);

        Double total2 = BOOKS.stream().collect(Collectors.summingDouble(Book::getPrice));
        log.info("{}testSumming{}", separation, separation);
        log.info("{}", total2);

        Long total3 = BOOKS.stream().collect(Collectors.summingLong(Book::getId));
        log.info("{}testSumming{}", separation, separation);
        log.info("{}", total3);


        IntSummaryStatistics total4 = BOOKS.stream().collect(Collectors.summarizingInt(Book::getQuantity));
        log.info("{}testSumming{}", separation, separation);
        log.info("{}", total4);

        int total11 = BOOKS.stream().mapToInt(Book::getQuantity).sum();
        log.info("{}testSumming{}", separation, separation);
        log.info("{}", total11);
    }

    @Test
    public void testGroupingBy() {
        // 一个参数
        Map<String, List<Book>> pressMap = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress));
        log.info("{}testGroupingBy{}", separation, separation);
        CollectorsTest.printLog(pressMap);

        // 两个参数
        Map<String, Set<String>> namesByPress = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress, Collectors.mapping(Book::getName, Collectors.toSet())));
        log.info("{}testGroupingBy{}", separation, separation);
        CollectorsTest.printLog(namesByPress);

        Map<String, Integer> totalByPress = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress, Collectors.summingInt(Book::getQuantity)));
        log.info("{}testGroupingBy{}", separation, separation);
        CollectorsTest.printLog(totalByPress);


        // 三个参数
        Map<String, Set<String>> namesByPressTreeMap = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress, TreeMap::new, Collectors.mapping(Book::getName, Collectors.toSet())));
        log.info("{}testGroupingBy{}", separation, separation);
        CollectorsTest.printLog(namesByPressTreeMap);
    }

    /**
     * 通过判断某些条件分片
     */
    @Test
    public void testPartitioningBy() {
        Double priceThreshold = 2.0;
        Map<Boolean, List<Book>> pricePartitioning = BOOKS.stream().collect(Collectors.partitioningBy(book -> book.getPrice() >= priceThreshold));
        log.info("{}testPartitioningBy{}", separation, separation);
        CollectorsTest.printLog(pricePartitioning);
    }


    /**
     * 根据出版社去重后的数据
     */
    @Test
    public void testCollectingAndThen() {
        List<Book> booksPress = BOOKS.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Book::getPress))), ArrayList::new));
        log.info("{}testCollectingAndThen{}", separation, separation);
        CollectorsTest.printLog(booksPress);
    }

    @Test
    public void testTeeing() {
        Map<String, Book> result1 = BOOKS.stream().collect(
                Collectors.teeing(
                        Collectors.maxBy(Comparator.comparing(Book::getQuantity)),
                        Collectors.minBy(Comparator.comparing(Book::getQuantity)),
                        (e1, e2) -> {
                            HashMap<String, Book> map = new HashMap<>();
                            map.put("MAX", e1.get());
                            map.put("MIN", e2.get());
                            return map;
                        }
                ));

        log.info("{}testTeeing{}", separation, separation);
        CollectorsTest.printLog(result1);

        Map<String, List<Book>> result2 = BOOKS.stream().collect(Collectors.teeing(
                Collectors.filtering(p -> p.getQuantity() % 2 == 0, Collectors.toList()),
                Collectors.filtering(p -> p.getQuantity() % 2 != 0, Collectors.toList()),
                (res1, res2) -> {
                    Map<String, List<Book>> map = new HashMap<>();
                    map.put("EvenBooks", res1);
                    map.put("OddBooks", res2);
                    return map;
                }));

        log.info("{}testTeeing{}", separation, separation);
        CollectorsTest.printLog(result2);



    }

    @Test
    public void testFiltering() {
        // 每个出版社分组，库存数量大于等于7 的数据，与下面的区别：即使不满足条件也会保留 key
        Map<String, List<Book>> result1 = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress, Collectors.filtering(book -> book.getQuantity() >= 7, Collectors.toList())));
        log.info("{}testFiltering{}", separation, separation);
        CollectorsTest.printLog(result1);


        // 每个出版社分组，库存数量大于等于7 的数据, 与上面的区别：不满足条件key也直接不存在
        Map<String, List<Book>> result2 = BOOKS.stream().filter(book -> book.getQuantity() >= 7).collect(Collectors.groupingBy(Book::getPress));
        log.info("{}testFiltering{}", separation, separation);
        CollectorsTest.printLog(result2);
    }

    /**
     * 复合用法：根据出版社分组后，只保留同一个作者的一本书信息
     */
    @Test
    public void testGroupingByAndCollectingAndThen() {
        Map<String, List<Book>> pressMap = BOOKS.stream().collect(Collectors.groupingBy(Book::getPress, Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Book::getAuthor))), ArrayList::new)));
        log.info("{}testGroupingByAndCollectingAndThen{}", separation, separation);
        CollectorsTest.printLog(pressMap);
    }
}
