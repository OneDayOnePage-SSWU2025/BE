package com.example.odop.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long bookId;

    private String imgUrl;

    private String bookTitle;

    private String author;

    private LocalDateTime createdDate;

}
