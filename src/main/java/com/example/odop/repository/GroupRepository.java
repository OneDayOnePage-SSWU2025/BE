package com.example.odop.repository;

import com.example.odop.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Groups, Long> {
    Optional<Groups> findByGroupId(Long groupId);
    Optional<Groups> findByGroupNameAndCode(String groupName, Integer code);
    boolean existsByGroupNameAndCode(String groupName, Integer code);

    long count();  // 전체 그룹 수 N
    long countByTotalBookGreaterThan(int totalBook);
    long countByTotalBookEquals(int totalBook);
}
