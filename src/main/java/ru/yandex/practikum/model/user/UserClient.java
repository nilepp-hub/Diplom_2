package ru.yandex.practikum.model.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.config.Config;

public class UserClient extends Config {
    private static final String REGISTER = "auth/register";
    private static final String LOGIN = "auth/login";
    private static final String USER = "auth/user";

    @Step("Register user")
    public ValidatableResponse create(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all()
                .assertThat();
    }

    @Step("Login user")
    public ValidatableResponse login(UserLogin userLogin) {
        return getSpec()
                .body(userLogin)
                .when()
                .post(LOGIN)
                .then().log().all()
                .assertThat();
    }

    @Step("Change user")
    public ValidatableResponse change(String accessToken, User user) {
        return getSpec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then().log().all()
                .assertThat();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .when()
                .delete(USER)
                .then().log().all()
                .assertThat();
    }
}
