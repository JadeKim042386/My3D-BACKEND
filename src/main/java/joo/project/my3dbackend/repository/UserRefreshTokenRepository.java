package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    @Query("select ua.userRefreshToken from UserAccount ua where ua.id = :id and ua.userRefreshToken.reissueCount < :count")
    Optional<UserRefreshToken> findByUserAccountIdAndReissueCountLessThan(@Param("id") long id, @Param("count") long count);
}
