package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    @EntityGraph(attributePaths = {"company", "subscribe"})
    Optional<UserAccount> findByEmail(String email);

    @EntityGraph(attributePaths = "company")
    Optional<UserAccount> findById(Long userAccountId);
}
