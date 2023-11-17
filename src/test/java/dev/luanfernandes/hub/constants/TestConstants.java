package dev.luanfernandes.hub.constants;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {
    public static final String ANY_STRING = "ANY_STRING";
    public static final UUID ANY_ID = randomUUID();
    public static final String ANY_ID_STRING = ANY_ID.toString();
    public static final String VALID_EMAIL = "teste@teste.com";
    public static final LocalDateTime ANY_LOCAL_DATE_TIME = now();
}
