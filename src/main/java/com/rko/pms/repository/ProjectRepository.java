package com.rko.pms.repository;

import com.rko.pms.domain.Project;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
