package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.MemberLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberLoginLogRepository extends JpaRepository<MemberLoginLog, Long> {
    Optional<MemberLoginLog> findByMember(Member member);

    List<MemberLoginLog> findByMemberOrderByLoginDateDesc(Member member);
}
