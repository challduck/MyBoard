package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByMemberId(Long member_id);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
