package com.icia.sdsd.dao;

import com.icia.sdsd.dto.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByGcBNumOrderByGcNumDesc(int gcBNum);
}
