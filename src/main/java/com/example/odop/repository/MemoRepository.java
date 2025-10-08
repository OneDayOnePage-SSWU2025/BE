package com.example.odop.repository;

import com.example.odop.entity.Books;
import com.example.odop.entity.Memos;
import com.example.odop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memos, Long> {
    Optional<Memos> findByMemoId(Long memoId);
    List<Memos> findAllByUserAndBook(Users user, Books book);
    Optional<Memos> findTopByUserAndBookOrderByCreatedDateDesc(Users user, Books book);
    List<Memos> findAllByBookAndPage(Books book, Integer page);
    int countByUser(Users user);

}
