package io.github.toquery.example.lambda.data;

import io.github.toquery.example.lambda.entity.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MockBookData {

    public static final MockBookData INSTANCE = new MockBookData();


    private MockBookData() {
    }

    /**
     * 图书数据，
     * <p>
     * 按出版社划分：化工出版社：Book 3，机械出版社：Book 1，电子出版社：Book 2，Book 4，Book 5
     * </p>
     * <p>
     * 按作者划分：John：Book 1，Book 4；Ethan：Book 2，Book 3，Book 5
     * </p>
     */
    public List<Book> books() {
        List<Book> books = new ArrayList<>();

        Book book1 = Book.builder()
                .id(1L).name("Book 1").author("John").price(1.1).quantity(6)
                .press("机械出版社")
                .publishDate(LocalDate.of(1987, 3, 20))
                .buyDateTime(LocalDateTime.of(2001, 6, 7, 11, 20, 33))
                .build();

        books.add(book1);

        Book book2 = Book.builder()
                .id(2L).name("Book 2").author("Ethan").price(2.1).quantity(3)
                .press("电子出版社")
                .publishDate(LocalDate.of(1997, 9, 22))
                .buyDateTime(LocalDateTime.of(2021, 5, 7, 16, 22, 13))
                .build();
        books.add(book2);

        Book book3 = Book.builder()
                .id(3L).name("Book 3").author("Ethan").price(3.3).quantity(7)
                .press("化工出版社")
                .publishDate(LocalDate.of(2018, 4, 12))
                .buyDateTime(LocalDateTime.of(2019, 10, 8, 12, 55, 24))
                .build();
        books.add(book3);


        Book book4 = Book.builder()
                .id(4L).name("Book 4").author("John").price(4.4).quantity(6)
                .press("电子出版社")
                .publishDate(LocalDate.of(2015, 6, 6))
                .buyDateTime(LocalDateTime.of(2022, 5, 27, 6, 27, 35))
                .build();
        books.add(book4);

        Book book5 = Book.builder()
                .id(5L).name("Book 5").author("Ethan").price(5.1).quantity(5)
                .press("电子出版社")
                .publishDate(LocalDate.of(1995, 5, 25))
                .buyDateTime(LocalDateTime.of(2015, 5, 5, 15, 25, 53))
                .build();
        books.add(book5);

        return books;
    }


}
