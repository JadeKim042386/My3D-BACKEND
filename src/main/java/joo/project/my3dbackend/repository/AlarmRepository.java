package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Alarm;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("""
            select distinct a
            from Alarm a
            left outer join fetch a.sender
            left outer join fetch a.article
            where a.receiverId=?1 and a.readAt is null
            """)
    List<Alarm> findAllByReceiverId(Long receiverId);
}
