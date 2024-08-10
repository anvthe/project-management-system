package com.rko.pms.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    PRE(0), START(1), END(2);

    private final int value;

    ProjectStatus(int value) {
        this.value = value;
    }

    public static ProjectStatus fromValue(int value) {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}
