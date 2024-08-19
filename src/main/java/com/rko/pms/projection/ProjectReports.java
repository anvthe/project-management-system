package com.rko.pms.projection;

import com.rko.pms.domain.enums.ProjectStatus;
import java.time.LocalDate;

public interface ProjectReports {
    Long getId();
    String getName();
    String getIntro();
    ProjectStatus getStatus();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getOwner();
    String getProjectMemberUsernames();
}
