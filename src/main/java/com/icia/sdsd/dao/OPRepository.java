package com.icia.sdsd.dao;

import com.icia.sdsd.dto.OperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OPRepository extends JpaRepository<OperatorEntity, String> {

    Optional<OperatorEntity> findBySoBusnum(String soBusnum);

    OperatorEntity findBySoNameAndSoPhone(String soName, String soPhone);

    Optional<OperatorEntity> findBySoBusnumAndSoPhone(String soBusnum, String soPhone);

    List<OperatorEntity> findBySoNameContainingIgnoreCase(String keyword);

    List<OperatorEntity> findBySoBusnumContainingIgnoreCase(String keyword);

    List<OperatorEntity> findBySoCompanyContainingIgnoreCase(String keyword);
}
