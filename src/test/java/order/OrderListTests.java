package order;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.order.Order;
import ru.yandex.practikum.model.order.OrderStatus;
import ru.yandex.practikum.model.user.User;
import ru.yandex.practikum.model.user.UserClient;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderListTests {
    private Order order;
    private OrderStatus orderStatus;
    private User user;
    private UserClient userClient;
    private boolean isDel;
    private String accessToken;
    private List<Object> actualNumber = new ArrayList<>();
    private List<Object> numberOrder = new ArrayList<>();

    @Before
    public void start() throws IndexOutOfBoundsException {
        user = User.getTestDataReg();
        userClient = new UserClient();
        order = Order.getOrder();
        orderStatus = new OrderStatus();
        accessToken = userClient
                .create(user)
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
        numberOrder.add(0, orderStatus.create(order, accessToken)
                .statusCode(SC_OK)
                .extract()
                .path("order.number"));
    }

    @Test
    @DisplayName("Order authorization user")
    public void getOrderAuthorizationUserTest() {
        actualNumber.add(orderStatus.getOrders(accessToken)
                .statusCode(SC_OK)
                .extract()
                .path("orders.number"));
        assertEquals(actualNumber.get(0), numberOrder);
    }

    @Test
    @DisplayName("Order not authorization user")
    public void getOrderNotAuthorizationUserTest() {
        String error = orderStatus
                .getOrders("")
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
        assertEquals("You should be authorised", error);
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
