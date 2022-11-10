package io.github.toquery.example.lambda.collectors;

import io.github.toquery.example.lambda.entity.Book;

/**
 *
 */
public class BookCollectors {
    public static Double computeValue(Book book) {
        return book.getPrice() * book.getQuantity();
    }
}
