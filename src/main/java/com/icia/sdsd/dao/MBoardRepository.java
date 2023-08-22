package com.icia.sdsd.dao;

import com.icia.sdsd.dto.MarketingRequestEntity;
import com.icia.sdsd.dto.MboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MBoardRepository extends JpaRepository<MboardEntity, Integer> {
    List<MboardEntity> findAllByOrderByMbNumDesc();

    Optional<MboardEntity> findByMbrNum(int mrNum);

}
