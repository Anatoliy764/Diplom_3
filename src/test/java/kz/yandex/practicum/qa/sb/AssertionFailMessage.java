package kz.yandex.practicum.qa.sb;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AssertionFailMessage {

    public static final String MISMATCH = "не соответствует ожиданиям";
    public static final String HTTP_STATUS_CODE_MISMATCH = "HTTP статус код " + MISMATCH;
    public static final String ERROR_MESSAGE_MISMATCH = "Сообщение об ошибке " + MISMATCH;
    public static final String USER_EMAIL_MISMATCH = "адрес электронной почты " + MISMATCH;

    public static final String REGISTER_BUTTON_UNCLICKABLE = "Кнопка \"Зарегистрироваться\" не кликабельна";

}
