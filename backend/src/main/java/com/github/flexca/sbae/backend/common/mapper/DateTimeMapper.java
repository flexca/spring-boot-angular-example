package com.github.flexca.sbae.backend.common.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper
public interface DateTimeMapper {

    DateTimeMapper INSTANCE = Mappers.getMapper(DateTimeMapper.class);

    DateTimeFormatter DATE_TIME_FORMATTER_UTC  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    default ZonedDateTime toZonedDateTime(Date input) {
        return ZonedDateTime.ofInstant(input.toInstant(), ZoneId.of("UTC"));
    }

    default ZonedDateTime toZonedDateTime(String input) {
        LocalDateTime localDateTime = StringUtils.isBlank(input) ? null : LocalDateTime.parse(input, DATE_TIME_FORMATTER_UTC);
        return ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
    }

    default ZonedDateTime toZonedDateTimeTrimTime(String input) {
        ZonedDateTime zonedDateTime = toZonedDateTime(input);
        if(zonedDateTime == null) {
            return null;
        }
        LocalDate localDate = zonedDateTime.toLocalDate();
        return ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(),
                0, 0, 0, 0, ZoneId.of("UTC"));
    }

    default Date toDate(ZonedDateTime input) {
        return input == null ? null : Date.from(input.toInstant());
    }
}
