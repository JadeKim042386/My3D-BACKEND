package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VerifyEmailRepository extends JpaRepository<VerifyEmail, Long> {
    @Query("select (ve.secretCode=:code) from VerifyEmail ve where ve.email=:email")
    boolean verifyCodeByEmail(@Param("email") String email, @Param("code") String secretCode);

    boolean existsByEmailAndExpiredAtLessThan(String email, LocalDateTime now);
}
