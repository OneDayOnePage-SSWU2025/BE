package com.example.odop.service;

import com.example.odop.dto.Request.GroupRequest;
import com.example.odop.dto.Response.*;
import com.example.odop.entity.*;
import com.example.odop.repository.BookRepository;
import com.example.odop.repository.GroupRepository;
import com.example.odop.repository.JoinRepository;
import com.example.odop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final JoinService joinService;
    private final JoinRepository joinRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void makeGroup(GroupRequest req, String id) {
        if(groupRepository.existsByGroupNameAndCode(req.getGroupName(), req.getCode())){
            throw new IllegalArgumentException("그룹 생성에 실패했습니다. 이미 존재하는 이름과 코드입니다. 둘 중 하나를 변경해주시기 바랍니다.");
        }
        Groups entity = new Groups();
        entity.setGroupName(req.getGroupName());
        entity.setCode(req.getCode());
        String theme = req.getTheme();
        entity.setTheme(theme);

        if(theme.equals("추리 소설")) {
            entity.setPetType(1);
        } else if (theme.equals("판타지 소설")) {
            entity.setPetType(2);
        } else {
            entity.setPetType(0);
        }

        entity.setTotalBook(1);
        Users user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        entity.setUser(user);

        groupRepository.save(entity);

        bookService.makeBook(req.getBook(), entity);

        joinService.makeJoinInternal(user, entity);
    }

    public Boolean isOwner(String id, Long groupId) {
        Groups group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        Users register = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return register.equals(group.getUser());

    }

    public GroupEditResponse getGroupForEdit(Long id){
        Groups group = groupRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 그룹입니다."));
        List<Books> books = bookRepository.findAllByGroup(group);
        GroupEditResponse res =  new GroupEditResponse();
        res.setGroupId(group.getGroupId());
        res.setGroupName(group.getGroupName());
        res.setTheme(group.getTheme());
        res.setCode(group.getCode());
        List<String> bookNames = new ArrayList<>();

        for (Books book : books) {
            bookNames.add(book.getBookTitle());
        }
        res.setBookNames(bookNames);
        return res;
    }


}
