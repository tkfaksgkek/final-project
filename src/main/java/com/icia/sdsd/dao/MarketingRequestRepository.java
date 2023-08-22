package com.icia.sdsd.dao;

import com.icia.sdsd.dto.MarketingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketingRequestRepository extends JpaRepository<MarketingRequestEntity, Integer> {

    List<MarketingRequestEntity> findAllByOrderByMrNumDesc();

    List<MarketingRequestEntity> findByMrResponseOrderByMrNumDesc(int mrResponse);

    Optional<MarketingRequestEntity> findByMrAno(String mrAno);
}
