package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    @BeforeEach
    void setUpBase(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

    }
}
