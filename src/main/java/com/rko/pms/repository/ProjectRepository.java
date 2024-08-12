package com.rko.pms.repository;

import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByStartDateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.rko.pms.dto.ProjectDTO(p.id, p.name, p.intro, p.owner.id, p.status, p.startDateTime, p.endDateTime, " +
            "u.username) FROM Project p JOIN p.projectMembers u WHERE p.startDateTime BETWEEN :start AND :end")
    List<ProjectDTO> findAllProjectsInBetweenDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
