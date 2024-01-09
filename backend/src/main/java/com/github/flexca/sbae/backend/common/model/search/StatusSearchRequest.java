package com.github.flexca.sbae.backend.common.model.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum StatusSearchRequest {

    ACTIVE("active"), DISABLED("disabled"), PENDING("pending");

    private static final Map<String, StatusSearchRequest> BY_NAME = new HashMap<>();
    static {
        for(StatusSearchRequest value : values()) {
            BY_NAME.put(value.getName(), value);
        }
    }

    private final String name;

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static StatusSearchRequest fromJsonString(String name) {
        return BY_NAME.get(name);
    }
}
