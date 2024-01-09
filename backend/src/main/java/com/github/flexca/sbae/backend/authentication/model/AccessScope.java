package com.github.flexca.sbae.backend.authentication.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum AccessScope {

    ORGANIZATION("organization-scope"),
    GLOBAL("global-scope");

    private static final Map<String, AccessScope> BY_NAME = new HashMap<>();
    static {
        for(AccessScope value : values()) {
            BY_NAME.put(value.getName(), value);
        }
    }

    private final String name;

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static AccessScope fromJsonString(String name) {
        return BY_NAME.get(name);
    }
}
