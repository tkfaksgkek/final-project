package com.icia.sdsd.dao;

import com.icia.sdsd.dto.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    List<MemberEntity> findBySmNickname(String smNickname);

    MemberEntity findBySmNameAndSmPhone(String smName, String smPhone);

    Optional<MemberEntity> findBySmIdAndSmPhone(String smId, String smPhone);

    MemberEntity findBySmEmail(String email);

    List<MemberEntity> findBySmIdContainingIgnoreCase(String keyword);

    List<MemberEntity> findBySmNicknameContainingIgnoreCase(String keyword);

}
