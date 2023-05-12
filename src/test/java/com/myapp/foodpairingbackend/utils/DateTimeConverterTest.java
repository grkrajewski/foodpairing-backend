package com.myapp.foodpairingbackend.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeConverterTest {

    @Test
    void testConvertLocalDateTimeToString() {
        //Given
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 12, 10, 12, 15);

        //When
        String convertedDateTime = DateTimeConverter.convertLocalDateTimeToString(dateTime);

        //Then
        assertEquals("2023-05-12 10:12:15", convertedDateTime);
    }

    @Test
    void testConvertStringToLocalDateTime() {
        //Given
        String dateTime = "2023-05-12 10:12:15";

        //When
        LocalDateTime convertedDateTime = DateTimeConverter.convertStringToLocalDateTime(dateTime);

        //Then
        assertEquals(convertedDateTime, LocalDateTime.of(2023, 5, 12, 10, 12, 15));
    }
}