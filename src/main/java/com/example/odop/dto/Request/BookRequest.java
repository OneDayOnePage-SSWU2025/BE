package com.example.odop.dto.Request;

import com.example.odop.entity.Groups;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest{

    private String imgUrl;

    private String bookTitle;

    private String author;

    private Integer totalPage;

   // private Boolean isTargeted;
}
