package kz.yandex.practicum.qa.sb.rest.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.ApiResponseValidator;
import lombok.experimental.UtilityClass;
import org.apache.http.HttpStatus;

@UtilityClass
public final class UserRestClient {

    @Step("Создание пользователя")
    public static User create(User user) throws ApiException {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        Response response = UserRestAssuredUtil.create(user);

        ApiResponseValidator.validate(response, HttpStatus.SC_OK);

        UserResponse userResponse = response.then().extract().as(UserResponse.class);
        userResponse.getUser().setPassword(user.getPassword());

        return userResponse.getUser();
    }

    @Step("Аутентификация пользователя")
    public static User login(String email, String password) throws ApiException {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email should be initialized");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password should be initialized");
        }
        Response response = UserRestAssuredUtil.login(email, password);

        ApiResponseValidator.validate(response, HttpStatus.SC_OK);

        UserResponse userResponse = response.then().extract().as(UserResponse.class);
        userResponse.getUser().setPassword(password);

        return userResponse.getUser();
    }

    @Step("Аутентификация пользователя")
    public static User login(User user) throws ApiException {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        return login(user.getEmail(), user.getPassword());
    }

    @Step("get info")
    public static User getInfo(String accessToken) throws kz.yandex.practicum.qa.sb.rest.common.ApiException {

        Response response = UserRestAssuredUtil.getInfo(accessToken);

        kz.yandex.practicum.qa.sb.rest.common.ApiResponseValidator.validate(response);

        UserResponse userResponse = response.then().extract().as(UserResponse.class);
        return userResponse.getUser().setAccessToken(accessToken);
    }

    @Step("delete user")
    public static void delete(User user) throws ApiException {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        delete(user.getAccessToken());
    }

    @Step("Удаление пользователя")
    public static void delete(String accessToken) throws ApiException {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("User access token should be initialized");
        }
        Response response = UserRestAssuredUtil.delete(accessToken);

        ApiResponseValidator.validate(response, HttpStatus.SC_ACCEPTED);
    }
}
