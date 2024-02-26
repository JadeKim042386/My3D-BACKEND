package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {}
