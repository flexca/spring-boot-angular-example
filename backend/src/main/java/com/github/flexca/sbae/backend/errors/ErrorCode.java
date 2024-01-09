package com.github.flexca.sbae.backend.errors;

public enum ErrorCode {

    SYSTEM_UNEXPECTED_ERROR,

    INVALID_ID,

    INVALID_CREDENTIALS,
    INVALID_REFRESH_TOKEN,
    SEARCH_EXCEED_MAX_LIMIT,

    USER_NOT_FOUND,
    USER_NOT_ACTIVE,
    USER_EMPTY_EMAIL,
    USER_INVALID_EMAIL,
    USER_EMAIL_ALREADY_USED,
    USER_EMAIL_EXCEED_MAX_LENGTH,
    USER_EMPTY_FIRST_NAME,
    USER_FIRST_NAME_EXCEED_MAX_LENGTH,
    USER_EMPTY_LAST_NAME,
    USER_LAST_NAME_EXCEED_MAX_LENGTH,
    USER_EMPTY_PERMISSIONS,
    USER_PERMISSION_CANNOT_BE_ASSIGNED,
    USER_PERMISSION_DUPLICATE,
    USER_EMPTY_PASSWORD,
    USER_PASSWORD_CONFORMATION_DIFFERENT,
    USER_PASSWORD_TOO_SHORT,
    USER_PASSWORD_EXCEED_MAX_LENGTH,
    USER_SELF_STATUS_CHANGE_ERROR,
    USER_ALREADY_CONFIRMED,
    USER_REGISTRATION_TOKEN_INVALID,
    USER_REGISTRATION_TOKEN_EXPIRED,
    USER_RESET_PASSWORD_TOKEN_INVALID,
    USER_RESET_PASSWORD_TOKEN_EXPIRED,

    SHA256_CALCULATION_ERROR,
    ENCRYPTION_KEY_FAILURE,
    ENCRYPTION_FAILURE,
    DECRYPTION_FAILURE,
    PEM_PARSE_FAILURE,
    KEYPAIR_GENERATION_FAILURE,
    UNSUPPORTED_KEY_TYPE,
    JWT_SIGNING_KEY_NOT_FOUND,
    MISSING_REQUIRED_PERMISSION,

    ORGANIZATION_NAME_BLANK,
    ORGANIZATION_NAME_MAX_LENGTH_EXCEED,
    ORGANIZATION_NAME_ALREADY_USED,
    ORGANIZATION_DESCRIPTION_MAX_LENGTH_EXCEED,
    NO_ACCESS_TO_ORGANIZATION,
    ORGANIZATION_NOT_FOUND,
    ORGANIZATION_NOT_ACTIVE,
    ORGANIZATION_ALREADY_CONFIRMED,
    ORGANIZATION_REGISTRATION_TOKEN_EXPIRED,
    ORGANIZATION_REGISTRATION_TOKEN_EMPTY,
    ORGANIZATION_REGISTRATION_TOKEN_INVALID,
    ORGANIZATION_INVALID_STATUS_CHANGE,
    ORGANIZATION_REGISTRATION_NOT_COMPLETED,

}