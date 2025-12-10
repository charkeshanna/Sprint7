package Tests;

import POJO.Courier;
import POJO.CourierCredentials;
import Utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Steps.CreateCourierSteps;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class authenticationTest {

    private boolean isCourierCreated = false;
    private int courierId;
    private String login;
    private String password;



    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }


    @AfterEach
    public void deleteCourier() {
        if (courierId != 0 && isCourierCreated) {
            CreateCourierSteps.deleteCourierById(courierId);
        }
    }


    @Test
    @DisplayName("Authentication with valid login and password")
    public void loginWithValidCredentialsReturnsIdAndSuccessResponse() {
        //Preconditions: create courier
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        CreateCourierSteps.addCourierReturnsSuccessResponse(login, password);

        //залогинемся
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравнив статус код
        CreateCourierSteps.checkStatusCode(response, 200);
        //вытянем courierID
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);
        isCourierCreated = true;
        assertNotNull(courierId, "ID не должен быть NULL");
        assertTrue(courierId > 0, "Id должен быть больше 0");
    }



    @Test
    @DisplayName("Login with not existing login")
    public void loginWithWrongLoginReturnsNotFoundError() {
        String randomLogin = DataGenerator.generatedCourierLogin();
        String randomPassword = DataGenerator.generateCourierPassword();

        CourierCredentials courierCredentials = new CourierCredentials(randomLogin, randomPassword);
        //получим респонс
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравнив статус код
        CreateCourierSteps.checkStatusCode(response, 404);
        CreateCourierSteps.checkResponseValue(response, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Login with invalid password")
    public void loginWithWrongPasswordReturnsNotFoundError() {
        //Preconditions: create courier
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        CreateCourierSteps.addCourierReturnsSuccessResponse(login, password);
        isCourierCreated=true;

        //залогинемся
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);

        //Сам тест
        //сгенерируем новый пароль
        String wrongPassword = DataGenerator.generateCourierPassword();
        //залогинимся с неправильным паролем
        CourierCredentials courierCredentialsWrong = new CourierCredentials(login, wrongPassword);
        Response responseAfterLogin = CreateCourierSteps.loginWithLoginAndPassword(courierCredentialsWrong);
        CreateCourierSteps.checkStatusCode(responseAfterLogin,404);
        CreateCourierSteps.checkResponseValue(responseAfterLogin, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Login without password")
    public void loginWithoutPasswordReturnsBadRequestError() {
        //Preconditions: create courier
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        CreateCourierSteps.addCourierReturnsSuccessResponse(login, password);
        isCourierCreated=true;

        //залогинемся
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);

        //login without  password
        CourierCredentials courierCredentialsNoPassword = new CourierCredentials(login, null);
        Response responseAfterLogin = CreateCourierSteps.loginWithLoginAndPassword(courierCredentialsNoPassword);
        CreateCourierSteps.checkStatusCode(responseAfterLogin,400);
        CreateCourierSteps.checkResponseValue(responseAfterLogin, "message", "Недостаточно данных для входа");

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
