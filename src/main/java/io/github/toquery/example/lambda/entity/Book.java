package io.github.toquery.example.lambda.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    private Long id;

    private String name;

    private String author;
    /**
     * 单价
     */
    private Double price;
    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 出版社
     */
    private String press;

    /**
     * 发行日期
     */
    private LocalDate publishDate;

    /**
     * 购买时间
     */
    private LocalDateTime buyDateTime;
}
