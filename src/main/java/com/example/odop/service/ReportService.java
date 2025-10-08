package com.example.odop.service;

import com.example.odop.dto.Response.MyPageResponse;
import com.example.odop.dto.Response.ReportResponse;
import com.example.odop.entity.Reports;
import com.example.odop.entity.Users;
import com.example.odop.repository.MemoRepository;
import com.example.odop.repository.ReportRepository;
import com.example.odop.repository.UserRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public void makeReport(Users user) {
        Reports entity = new Reports();
        entity.setUser(user);
        entity.setBookCount(0);
        reportRepository.save(entity);

    }

    @Transactional
    public void updateReport(Users user) {
        Reports report = reportRepository.findByUser(user).orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저"));
        report.setBookCount(report.getBookCount() + 1);
    }

    @Transactional
    public ReportResponse getMyReport(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        Reports report = reportRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        ReportResponse res = new ReportResponse();
        res.setTotalBook(report.getBookCount());
        res.setTotalMemo(memoRepository.countByUser(user));
        return res;
    }
}
