package com.example.odop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gropus")
public class Groups extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;
    @Column(nullable = false)
    private String groupName;
    @Column(nullable = false)
    private Integer code;
    @Column(nullable = false)
    private String theme;
    @Column(nullable = false)
    private Integer totalBook;

    //0 -> 기본숭이, 1->추리캣, 2->판타지토끼
    @Column(nullable = false)
    private Integer petType;
}
