package ru.yandex.practikum.config;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {
    public static String URI = "https://stellarburgers.nomoreparties.site/api/";

    protected RequestSpecification getSpec(){
        return given().log().all()
                .contentType("application/json")
                .baseUri(URI);
    }
}
