package ru.yandex.practikum.model.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.config.Config;

public class OrderStatus extends Config {
    private static final String ORDER = "orders";

    @Step("Order received")
    public ValidatableResponse create(Order order, String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER)
                .then()
                .log().all()
                .assertThat();
    }

    @Step("Order list")
    public ValidatableResponse getOrders(String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .get(ORDER)
                .then()
                .log().all()
                .assertThat();
    }
}
