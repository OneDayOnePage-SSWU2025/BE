package com.example.odop.service;

import com.example.odop.dto.Request.BookRequest;
import com.example.odop.entity.Books;
import com.example.odop.entity.Groups;
import com.example.odop.entity.Joins;
import com.example.odop.entity.Users;
import com.example.odop.repository.BookRepository;
import com.example.odop.repository.GroupRepository;
import com.example.odop.repository.JoinRepository;
import com.example.odop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GroupRepository groupRepository;
    private final ReportService reportService;
    private final JoinRepository joinRepository;
    private final UserRepository userRepository;

    @Transactional
    public void makeBook(BookRequest req, Groups group) {
        Books entity = new Books();
        entity.setBookTitle(req.getBookTitle());
        entity.setGroup(group);
        entity.setImgUrl(req.getImgUrl());
        entity.setTotalPage(req.getTotalPage());
        entity.setIsTargeted(true);
        entity.setAuthor(req.getAuthor());

        bookRepository.save(entity);
    }

    @Transactional
    public void addBook(String id, BookRequest req, Long groupId) throws IllegalAccessException {
        Books oldBook = bookRepository.findByIsTargeted(true);
        if (oldBook != null) {
            oldBook.setIsTargeted(false);
        }

        Groups group = groupRepository.findByGroupId(groupId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        Users register = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        if(register != group.getUser()){
            throw new IllegalAccessException("생성자만 책을 수정할 수 있습니다.");
        }

        group.setTotalBook(group.getTotalBook() + 1);

        Books entity = new Books();
        entity.setBookTitle(req.getBookTitle());
        entity.setAuthor(req.getAuthor());
        entity.setGroup(group);
        entity.setImgUrl(req.getImgUrl());
        entity.setTotalPage(req.getTotalPage());
        entity.setIsTargeted(true);

        bookRepository.save(entity);

        //그룹에 속한 모든 참가자의 리포트 +1
        List<Joins> joins = joinRepository.findAllByGroup(group);
        for (Joins join : joins) {
            Users user = join.getUser();
            reportService.updateReport(user);
        }
    }
    @Transactional
    public Integer getBookTotalPage(Long bookId) {
        return bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다.")).getTotalPage();
    }
}
