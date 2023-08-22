package com.icia.sdsd.dao;

import com.icia.sdsd.dto.TicketingDTO;
import com.icia.sdsd.dto.TicketingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface STRepository extends JpaRepository<TicketingEntity, String> {

    // @Modifying
    // @Transactional
    // @Query(value = "insert into SDTICKETING values('reserv_no_'||STNUM_SEQ.NEXTVAL,:#{#entity.stId},:#{#entity.stCode},:#{#entity.stStartDay},:#{#entity.stEndDay},:#{#entity.stPay},:#{#entity.stAmount})", nativeQuery = true)
    // void insertDB(@Param("entity") TicketingEntity entity);

    List<TicketingEntity> findByStId(String id);

    List<TicketingEntity> findByStPayOrderByStDateDesc(int keyword);

    List<TicketingEntity> findByStPayOrStPayOrderByStDateDesc(int a, int b);

    List<TicketingEntity> findByStIdContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(String keyword, int a, int b);

    List<TicketingEntity> findByStNumContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(String keyword, int a, int b);
}
