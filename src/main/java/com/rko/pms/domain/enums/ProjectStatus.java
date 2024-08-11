package com.rko.pms.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    PRE(0), START(1), END(2);

    private final int value;

    ProjectStatus(int value) {
        this.value = value;
    }
}
