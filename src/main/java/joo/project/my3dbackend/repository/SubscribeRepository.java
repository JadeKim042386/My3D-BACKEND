package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    Optional<Subscribe> findByUserAccount_Id(Long userAccountId);
}
