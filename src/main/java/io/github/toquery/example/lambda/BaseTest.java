package io.github.toquery.example.lambda;

import com.alibaba.fastjson2.JSON;
import io.github.toquery.example.lambda.data.MockBookData;
import io.github.toquery.example.lambda.entity.Book;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class BaseTest {
    protected static final String separation = "--------";
    protected static final List<Book> BOOKS = MockBookData.INSTANCE.books();

    /**
     * 打印日志
     */
    public static void printLog(Iterable<?> list) {
        list.forEach(item -> log.info(JSON.toJSONString(item)));
    }

    public static void printLog(Map<?, ?> map) {
        map.forEach((k, v) -> log.info("{} : {}", k, JSON.toJSONString(v)));
    }
}
