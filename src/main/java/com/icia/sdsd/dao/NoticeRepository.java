package com.icia.sdsd.dao;

import com.icia.sdsd.dto.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    List<NoticeEntity> findAllByOrderBySnUpdateDateDesc();
}
