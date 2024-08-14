package com.rko.pms.repository;

import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByStartDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT new com.rko.pms.dto.ProjectDTO(p.id, p.name, p.intro, p.owner.id, p.status, p.startDate, p.endDate, " +
            "u.username) FROM Project p JOIN p.projectMembers u WHERE p.startDate BETWEEN :start AND :end")
    List<ProjectDTO> findAllProjectsInBetweenDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
