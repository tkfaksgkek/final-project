package com.icia.sdsd.dao;

import com.icia.sdsd.dto.GBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface GBoardRepository extends JpaRepository<GBoardEntity, Integer> {
    List<GBoardEntity> findAllByOrderByGbNumDesc();

//    @Modifying
//    @Transactional
//    @Query("UPDATE GBoardEntity b SET b.gbHit = b.gbHit + 1 WHERE b.gbNum = :gbNum")
//    void gbHit(@Param("gbNum")int gbNum);

    // 조회수 순서대로 자유게시판 목록 불러오기
    List<GBoardEntity> findAllByOrderByGbHitDesc();

    List<GBoardEntity> findByGbTitleContainingIgnoreCaseOrderByGbNumDesc(String keyword);

    List<GBoardEntity> findByGbContentContainingIgnoreCaseOrderByGbNumDesc(String keyword);

    List<GBoardEntity> findByGbWriterContainingIgnoreCaseOrderByGbNumDesc(String keyword);

    List<GBoardEntity> findByGbWriterIdContainingIgnoreCaseOrderByGbNumDesc(String gbWriterId);
}
