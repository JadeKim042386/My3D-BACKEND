package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
