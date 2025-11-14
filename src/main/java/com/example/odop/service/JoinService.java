package com.example.odop.service;

import com.example.odop.dto.Response.ApiListResponse;
import com.example.odop.dto.Response.BookResponse;
import com.example.odop.dto.Response.GroupDetailResponse;
import com.example.odop.dto.Response.GroupResponse;
import com.example.odop.entity.*;
import com.example.odop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class JoinService {
    private final JoinRepository joinRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public void makeJoinInternal(Users user, Groups group) {
        Joins entity = new Joins();
        entity.setUser(user);
        entity.setGroup(group);
        joinRepository.save(entity);

    }
    @Transactional
    public void makeJoin(String id, String groupName, Integer code) {
        Users user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Groups group = groupRepository.findByGroupNameAndCode(groupName, code).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 그룹입니다."));
        makeJoinInternal(user, group);
    }

    @Transactional
    public ApiListResponse<GroupResponse> getGroups(String id){
        Users user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        List<Joins> joins = joinRepository.findAllByUser(user);
        List<GroupResponse> groupResponses = new ArrayList<>();

        for (Joins join : joins) {
            Groups group = join.getGroup();
            GroupResponse res = new GroupResponse();
            res.setGroupId(group.getGroupId());
            res.setGroupName(group.getGroupName());
            res.setPetType(group.getPetType());
            res.setTotalBook(group.getTotalBook());
            res.setPercentage(calcTopPercent(group));
            groupResponses.add(res);
        }
        return new ApiListResponse<>(true, "그룹 조회 성공", groupResponses.size(), groupResponses);
    }

    @Transactional
    public ApiListResponse<GroupDetailResponse> getDetailedGroups(String id){
        Users user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        List<Joins> joins = joinRepository.findAllByUser(user);
        List<GroupDetailResponse> groupDetailResponses = new ArrayList<>();

        for (Joins join : joins) {
            Groups group = join.getGroup();
            GroupDetailResponse res = new GroupDetailResponse();
            res.setGroupId(group.getGroupId());
            res.setGroupName(group.getGroupName());
            log.info("groupId={}, isTargeted={}", group.getGroupId(), true);
            Books book = bookRepository.findByGroupAndIsTargeted(group, true).orElseThrow(()->new IllegalArgumentException("책이 존재하지 않음"));
            BookResponse bookRes = new BookResponse();
            bookRes.setBookTitle(book.getBookTitle());
            bookRes.setAuthor(book.getAuthor());
            bookRes.setImgUrl(book.getImgUrl());
            bookRes.setCreatedDate(book.getCreatedDate());
            bookRes.setBookId(book.getBookId());
            res.setBook(bookRes);

            Optional<Memos> memoOpt =
                    memoRepository.findTopByUserAndBookOrderByCreatedDateDesc(user, book);

            int latestPage = memoOpt.map(Memos::getPage).orElse(0);
            res.setLatestMemoPage(latestPage);

            groupDetailResponses.add(res);
        }
        return new ApiListResponse<>(true, "그룹 조회 성공", groupDetailResponses.size(), groupDetailResponses);
    }

    @Transactional(readOnly = true)
    public Integer calcTopPercent(Groups g) {
        int mine = g.getTotalBook();

        long N = groupRepository.count();
        if (N == 0) return 0;

        long greater = groupRepository.countByTotalBookGreaterThan(mine);
        long equal   = groupRepository.countByTotalBookEquals(mine);

        // 동점 중간치 처리(권장)
        double rankFromTop = greater + Math.max(0, (equal - 1)) * 0.5;
        double topPercent = (rankFromTop / N) * 100.0;

        // 소수점 2자리 반올림
        return (int) Math.round(topPercent);
    }

}
