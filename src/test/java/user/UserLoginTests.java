package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.testdata.UserTestData;
import ru.yandex.practikum.model.user.User;
import ru.yandex.practikum.model.user.UserClient;
import ru.yandex.practikum.model.user.UserLogin;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserLoginTests {
    User user;
    UserClient userClient;
    String accessToken;

    @Before
    public void start() {
        user = User.getTestDataReg();
        userClient = new UserClient();
        accessToken = userClient
                .create(user)
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("login registered user")
    public void loginUnderAnExistingUserTest() {
        boolean isReg = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_OK)
                .extract()
                .path("success");
        assertTrue(isReg);
    }

    @Test
    @DisplayName("login not valid Email")
    public void loginIncorrectEmailTest() {
        user.setEmail(UserTestData.getRndString(4));
        String error = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("email or password are incorrect", error);
    }

    @Test
    @DisplayName("login not valid Pass")
    public void loginIncorrectPassTest() {
        user.setPassword(UserTestData.getRndString(4));
        String error = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("email or password are incorrect", error);
    }

    @After
    public void finish() {
        boolean isDel = userClient
                .delete(accessToken)
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("success");
        assertTrue(isDel);
    }
}
