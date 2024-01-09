package com.github.flexca.sbae.backend.authentication.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public enum SbaePermission {

    PERMISSION_ORGANIZATION_VIEW("permission_organization_view"),
    PERMISSION_ORGANIZATION_MANAGE("permission_organization_manage"),
    PERMISSION_USER_VIEW("permission_user_view"),
    PERMISSION_USER_MANAGE("permission_user_manage");

    private static final Map<String, SbaePermission> BY_NAME = new HashMap<>();
    static {
        for(SbaePermission value : values()) {
            BY_NAME.put(value.getName(), value);
        }
    }

    private final String name;

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static SbaePermission fromJsonString(String name) {
        return name == null ? null : BY_NAME.get(name.toLowerCase());
    }

    public static List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for(SbaePermission value : values()) {
            names.add(value.getName());
        }
        return names;
    }
}
