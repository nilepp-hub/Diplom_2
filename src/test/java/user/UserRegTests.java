package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.user.User;
import ru.yandex.practikum.model.user.UserClient;
import ru.yandex.practikum.model.user.UserLogin;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRegTests {
    private User user;
    private UserClient userClient;
    private boolean isDel;
    private String accessToken;
    private boolean isReg;

    @Before
    public void start() {
        user = User.getTestDataReg();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Reg random user ")
    public void regRndUserTest() {
        isReg = userClient
                .create(user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        assertTrue(isReg);
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Reg Repeat user")
    public void regRepeatUserTest() {
        userClient.create(user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        String error = userClient
                .create(user)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
        assertEquals("User already exists", error);
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Reg user without a name")
    public void regUserNotNameTest() {
        user.setName("");
        String error = userClient
                .create(user)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
        assertEquals("Email, password and name are required fields", error);
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Reg user without a pass")
    public void regUserNotPassTest() {
        user.setPassword("");
        String error = userClient
                .create(user)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
        assertEquals("Email, password and name are required fields", error);
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Reg user without a email")
    public void regUserNotEmailTest() {
        user.setEmail("");
        String error = userClient
                .create(user)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
        assertEquals("Email, password and name are required fields", error);
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .extract()
                .path("accessToken");
    }

    @After
    public void finish() {
        if (accessToken != null) {
            isDel = userClient
                    .delete(accessToken)
                    .statusCode(SC_ACCEPTED)
                    .extract()
                    .path("success");
            assertTrue(isDel);
        }
    }
}
