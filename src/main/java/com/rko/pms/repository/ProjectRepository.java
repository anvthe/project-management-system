package com.rko.pms.repository;

import com.rko.pms.domain.Project;
import com.rko.pms.domain.enums.ProjectStatus;
import com.rko.pms.dto.ProjectDTO;
import com.rko.pms.projection.ProjectReports;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByStartDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT (p.id, p.name, p.intro, p.owner.id, p.status, p.startDate, p.endDate, " +
           "u.username) FROM Project p JOIN p.projectMembers u WHERE p.startDate BETWEEN :start AND :end")
    List<ProjectDTO> findAllProjectsInBetweenDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = "SELECT p.id AS id, " +
            "p.name AS project_name, " +
            "p.intro AS intro, " +
            "p.status AS status, " +
            "p.start_date AS start_date, " +
            "p.end_date AS end_date, " +
            "u.name AS owner_name, " +
            "GROUP_CONCAT(um.name ORDER BY um.name SEPARATOR ', ') AS projectMemberUsernames " +
            "FROM projects p " +
            "LEFT JOIN project_members pm ON p.id = pm.project_id " +
            "LEFT JOIN users u ON p.owner_id = u.id " +
            "LEFT JOIN users um ON pm.user_id = um.id " +
            "GROUP BY p.id, p.name, p.intro, p.status, p.start_date, p.end_date, u.name",
            nativeQuery = true)
    List<ProjectReports> getReports();



    @Query("SELECT p FROM Project p JOIN p.projectMembers m GROUP BY p.id")
   List<Project> getProjectsWithMembers();

//    @Query("SELECT p.id AS id, p.name AS name, p.intro AS intro, p.status AS status, p.startDate AS startDate, p.endDate AS endDate, p.projectMemberUsernames AS projectMemberUsernames, p.owner.username AS owner " +
//            "FROM Project p WHERE p.id = :projectId")
//    Optional<ProjectReports> findProjectDTOById(@Param("projectId") Long projectId);




}
