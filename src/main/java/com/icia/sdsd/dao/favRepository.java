package com.icia.sdsd.dao;


import com.icia.sdsd.dto.TicketingEntity;
import com.icia.sdsd.dto.favEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface favRepository extends JpaRepository<favEntity, Integer> {
    Optional<favEntity> findByFaCodeAndFaLoginId(String faCode, String faLoginId);

    List<favEntity> findByFaLoginIdOrderByFaNumDesc(String faLoginId);

    // @Modifying
    // @Transactional
    // @Query(value = "insert into SDTICKETING values('reserv_no_'||STNUM_SEQ.NEXTVAL,:#{#entity.stId},:#{#entity.stCode},:#{#entity.stStartDay},:#{#entity.stEndDay},:#{#entity.stPay},:#{#entity.stAmount})", nativeQuery = true)
    // void insertDB(@Param("entity") TicketingEntity entity);


}
