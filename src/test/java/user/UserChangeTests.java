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

public class UserChangeTests {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private String noToken = "";
    private boolean isReg;

    @Before
    public void start() {
        user = User.getTestDataReg();
        userClient = new UserClient();
        isReg = userClient
                .create(user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        accessToken = userClient
                .login(UserLogin.getLogin(user))
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Reg and email change")
    public void regChangeEmailTest() {
        user.setEmail(UserTestData.rndEmail());
        boolean isTrue = userClient
                .change(accessToken, user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        assertTrue(isTrue);
    }

    @Test
    @DisplayName("Reg and password change")
    public void regChangePasswordTest() {
        user.setPassword(UserTestData.rndPassword());
        boolean isTrue = userClient
                .change(accessToken, user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        assertTrue(isTrue);
    }

    @Test
    @DisplayName("Reg and name change")
    public void regChangeNameTest() {
        user.setName(UserTestData.rndName());
        boolean isTrue = userClient
                .change(accessToken, user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        assertTrue(isTrue);
    }

    @Test
    @DisplayName("No Reg and email change")
    public void noRegChangeEmailTest() {
        user.setEmail(UserTestData.rndEmail());
        String error = userClient
                .change(noToken, user)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("You should be authorised", error);
    }

    @Test
    @DisplayName("No Reg and password change")
    public void noRegChangePasswordTest() {
        user.setPassword(UserTestData.rndPassword());
        String error = userClient
                .change(noToken, user)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("You should be authorised", error);
    }

    @Test
    @DisplayName("No Reg and name change")
    public void noRegChangeNameTest() {
        user.setName(UserTestData.rndName());
        String error = userClient
                .change(noToken, user)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("You should be authorised", error);
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
