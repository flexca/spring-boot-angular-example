package com.github.flexca.sbae.backend.common.model.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum Status {

    ACTIVE("active"),
    DISABLED("disabled"),
    PENDING("pending");

    private static final Map<String, Status> BY_NAME = new HashMap<>();
    static {
        for(Status value : values()) {
            BY_NAME.put(value.getName(), value);
        }
    }

    private final String name;

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static Status fromJsonString(String name) {
        return name == null ? null : BY_NAME.get(name.toLowerCase());
    }
}
