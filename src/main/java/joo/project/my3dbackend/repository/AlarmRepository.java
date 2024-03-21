package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {}
