package order;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.order.Order;
import ru.yandex.practikum.model.order.OrderStatus;
import ru.yandex.practikum.model.user.User;
import ru.yandex.practikum.model.user.UserClient;
import ru.yandex.practikum.model.user.UserLogin;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderRegTests {
    Order order;
    OrderStatus orderStatus;
    User user;
    UserClient userClient;
    String accessToken;
    boolean isReg;
    boolean isDel;

    @Before
    public void start() {
        order = Order.getOrder();
        orderStatus = new OrderStatus();
        userClient = new UserClient();
        user = User.getTestDataReg();
        isReg = userClient
                .create(user)
                .statusCode(SC_OK)
                .extract()
                .path("success");
        accessToken = userClient.login(UserLogin.getLogin(user))
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
        assertTrue(isReg);
    }

    @Test
    @DisplayName("Reg order with ingredients and authorization")
    public void regOrderWithIngredientsAuthorizationTest() {
        String name = orderStatus
                .create(order, accessToken)
                .statusCode(SC_OK)
                .extract()
                .path("name");
        assertEquals("Флюоресцентный бургер", name);
    }

    @Test
    @DisplayName("Reg order not ingredients")
    public void regOrderNoIngredientsTest() {
        order.setIngredients(new String[]{});
        String error = orderStatus
                .create(order, accessToken)
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .path("message");
        assertEquals("Ingredient ids must be provided", error);
    }

    @Test
    @DisplayName("Reg order not authorization")
    public void regOrderNoAuthorizationTest() {
        String name = orderStatus
                .create(order, "")
                .statusCode(SC_OK)
                .extract()
                .path("name");
        assertEquals("Флюоресцентный бургер", name);
    }

    @Test
    @DisplayName("Reg order with incorrect hash of ingredients")
    public void regOrderWithIncorrectHashIngredients() {
        order.setIngredients(new String[]{"no valid"});
        orderStatus.create(order, accessToken)
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    @After
    public void finish() {
        isDel = userClient
                .delete(accessToken)
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("success");
        assertTrue(isDel);
    }
}
