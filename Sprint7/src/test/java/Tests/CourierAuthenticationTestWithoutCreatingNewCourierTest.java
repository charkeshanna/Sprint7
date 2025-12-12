package Tests;

import POJO.CourierCredentials;
import Utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CreateCourierSteps;

public class CourierAuthenticationTestWithoutCreatingNewCourierTest {
    private String password;
    CreateCourierSteps createCourierSteps;
    @BeforeEach
    public void setUp() {
        createCourierSteps = new CreateCourierSteps();
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Authentication with not existing login")
    public void loginWithWrongLoginReturnsNotFoundError() {
        String randomLogin = DataGenerator.generateCourierLogin();
        String randomPassword = DataGenerator.generateCourierPassword();

        CourierCredentials courierCredentials = new CourierCredentials(randomLogin, randomPassword);
        //получим ответ
        Response response = createCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравнив статус код
        createCourierSteps.checkStatusCode(response, 404);
        createCourierSteps.checkResponseValue(response, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Authentication without login")
    public void loginWithoutLoginReturnsBadRequestError() {
        password = DataGenerator.generateCourierPassword();
        //login without login
        CourierCredentials courierCredentials = new CourierCredentials(null, password);
        Response responseAfterLogin = createCourierSteps.loginWithLoginAndPassword(courierCredentials);
        createCourierSteps.checkStatusCode(responseAfterLogin,400);
        createCourierSteps.checkResponseValue(responseAfterLogin, "message", "Недостаточно данных для входа");

    }

}
