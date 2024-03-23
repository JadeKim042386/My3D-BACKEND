package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    @Query("select ua.userRefreshToken from UserAccount ua where ua.id = ?1 and ua.userRefreshToken.reissueCount < ?2")
    Optional<UserRefreshToken> findByUserAccountIdAndReissueCountLessThan(long id, long count);
}
