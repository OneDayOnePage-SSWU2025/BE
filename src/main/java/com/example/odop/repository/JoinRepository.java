package com.example.odop.repository;

import com.example.odop.entity.Groups;
import com.example.odop.entity.Joins;
import com.example.odop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRepository extends JpaRepository<Joins, Long> {
    List<Joins> findAllByGroup(Groups group);
    List<Joins> findAllByUser(Users user);
    int countAllByUser(Users user);
}
