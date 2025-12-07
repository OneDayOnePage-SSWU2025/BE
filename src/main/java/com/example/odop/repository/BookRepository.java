package com.example.odop.repository;

import com.example.odop.entity.Books;
import com.example.odop.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Books, Long> {
    Books findByIsTargeted(Boolean isTargeted);
    Optional<Books> findByGroupAndIsTargeted(Groups group, Boolean isTargeted);
    Optional<Books> findByBookId(Long bookId);

    List<Books> findAllByGroup(Groups group);
}
