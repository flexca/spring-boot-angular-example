package com.github.flexca.sbae.backend.common.service;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DateProvider {

    public ZonedDateTime currentZonedDateTime() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
