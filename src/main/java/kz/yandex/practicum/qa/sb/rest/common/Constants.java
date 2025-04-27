package kz.yandex.practicum.qa.sb.rest.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String STELLAR_BURGERS_API_BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    public static final String STELLAR_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String STELLAR_BURGERS_REGISTER_URL = STELLAR_BURGERS_BASE_URL + "/register";
    public static final String STELLAR_BURGERS_FORGOT_PASSWORD_URL = STELLAR_BURGERS_BASE_URL + "/forgot-password";
    public static final String STELLAR_BURGERS_LOGIN_URL = STELLAR_BURGERS_BASE_URL + "/login";

    // API error messages
    public static final String ERROR_MESSAGE_INVALID_CREDENTIALS = "email or password are incorrect";
    public static final String ERROR_MESSAGE_SHOULD_BE_AUTHORIZED = "You should be authorised";
    public static final String ERROR_MESSAGE_USER_NOT_FOUND = "User not found";

}
