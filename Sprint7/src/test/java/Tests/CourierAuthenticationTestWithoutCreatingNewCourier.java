package Tests;

import POJO.CourierCredentials;
import Utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CreateCourierSteps;

public class CourierAuthenticationTestWithoutCreatingNewCourier {
    private String password;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Login with not existing login")
    public void loginWithWrongLoginReturnsNotFoundError() {
        String randomLogin = DataGenerator.generateCourierLogin();
        String randomPassword = DataGenerator.generateCourierPassword();

        CourierCredentials courierCredentials = new CourierCredentials(randomLogin, randomPassword);
        //получим ответ
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравнив статус код
        CreateCourierSteps.checkStatusCode(response, 404);
        CreateCourierSteps.checkResponseValue(response, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Login without login")
    public void loginWithoutLoginReturnsBadRequestError() {
        password = DataGenerator.generateCourierPassword();
        //login without login
        CourierCredentials courierCredentials = new CourierCredentials(null, password);
        Response responseAfterLogin = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        CreateCourierSteps.checkStatusCode(responseAfterLogin,400);
        CreateCourierSteps.checkResponseValue(responseAfterLogin, "message", "Недостаточно данных для входа");

    }

}
