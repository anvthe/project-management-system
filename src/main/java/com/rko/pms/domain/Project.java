package com.rko.pms.domain;

import com.rko.pms.domain.enums.ProjectStatus;
import com.rko.pms.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String intro;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> projectMembers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Transient
    private Long ownerId;

    @Transient
    private Set<Long> projectMemberIds;

    public void assignUsers(UserRepository userRepository) {
        if (ownerId != null) {
            this.owner = userRepository.findById(ownerId).orElse(null);
        }
        if (projectMemberIds != null && !projectMemberIds.isEmpty()) {
            this.projectMembers = userRepository.findAllById(projectMemberIds).stream().collect(Collectors.toSet());
        }
    }
}
