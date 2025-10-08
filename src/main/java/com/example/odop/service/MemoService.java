package com.example.odop.service;

import com.example.odop.dto.Request.MemoRequest;
import com.example.odop.dto.Response.ApiListResponse;
import com.example.odop.dto.Response.GroupDetailResponse;
import com.example.odop.dto.Response.MemoResponse;
import com.example.odop.entity.*;
import com.example.odop.repository.BookRepository;
import com.example.odop.repository.GroupRepository;
import com.example.odop.repository.MemoRepository;
import com.example.odop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void makeMemo(String id, MemoRequest req) {
        Users user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Books book = bookRepository.findByBookId(req.getBookId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        Memos entity = new Memos();
        entity.setUser(user);
        entity.setBook(book);
        entity.setMemo(req.getMemo());
        entity.setPage(req.getPage());
        memoRepository.save(entity);
    }

    @Transactional
    public ApiListResponse<MemoResponse> getMemosByPage(Long bookId, Integer page) {
        Books book = bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        List<Memos> memos = memoRepository.findAllByBookAndPage(book, page);
        List<MemoResponse> memoResponses = new ArrayList<>();
        for (Memos memo : memos) {
            MemoResponse res = new MemoResponse();
            Users user = memo.getUser();
            res.setMemoId(memo.getMemoId());
            res.setMemo(memo.getMemo());
            res.setImgUrl(user.getImgUrl());
            res.setNickName(user.getNickName());
            res.setPage(memo.getPage());
            memoResponses.add(res);
        }
        return new ApiListResponse<>(true, "페이지별 메모 목록 조회", memoResponses.size(), memoResponses);
    }

    @Transactional
    public ApiListResponse<MemoResponse> getMemosByMe(String id, Long bookId) {
        Books book = bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        Users user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        List<Memos> memos = memoRepository.findAllByUserAndBook(user, book);
        List<MemoResponse> memoResponses = new ArrayList<>();
        for (Memos memo : memos) {
            MemoResponse res = new MemoResponse();
            String imagUrl = memo.getUser().getImgUrl();
            res.setMemoId(memo.getMemoId());
            res.setMemo(memo.getMemo());
            res.setImgUrl(imagUrl);
            res.setPage(memo.getPage());
            memoResponses.add(res);
        }
        return new ApiListResponse<>(true, "페이지별 메모 목록 조회", memoResponses.size(), memoResponses);
    }

    @Transactional
    public MemoResponse getMemo(Long memoId) {

        Memos memo = memoRepository.findByMemoId(memoId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 메모입니다."));
        MemoResponse res = new MemoResponse();

        String imagUrl = memo.getUser().getImgUrl();
        res.setMemoId(memo.getMemoId());
        res.setMemo(memo.getMemo());
        res.setImgUrl(imagUrl);
        res.setPage(memo.getPage());

        return res;
    }

    @Transactional
    public Boolean isMine (String id, Long memoId) {

        Memos memo = memoRepository.findByMemoId(memoId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 메모입니다."));
        Users user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return memo.getUser().equals(user);
    }

    @Transactional
    public void deleteMemo(String id, Long memoId) {
        Memos memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메모입니다."));
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 권한 검사 (본인 메모인지)
        if (!memo.getUser().equals(user)) {
            throw new SecurityException("삭제 권한이 없습니다. 본인의 메모만 삭제할 수 있습니다.");
        }

        memoRepository.delete(memo);
    }

}
