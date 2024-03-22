package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VerifyEmailRepository extends JpaRepository<VerifyEmail, Long> {
    @Query("select (ve.secretCode=:code) from VerifyEmail ve where ve.email=:email and ve.expiredAt < now()")
    boolean verifyCodeByEmail(@Param("email") String email, @Param("code") String secretCode);
}
