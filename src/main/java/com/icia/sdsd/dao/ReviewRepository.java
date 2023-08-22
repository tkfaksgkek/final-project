package com.icia.sdsd.dao;


import com.icia.sdsd.dto.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String> {

    List<ReviewEntity> findBySrCodeOrderBySrNum(String srCode);
}
