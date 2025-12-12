package Tests;
import POJO.CourierCredentials;
import Utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CreateCourierSteps;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierAuthenticationTest {

    private boolean isCourierCreated = false;
    private int courierId;
    private String login;
    private String password;



    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        //сгенерируем логин и пароль
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создадим курьера
        CreateCourierSteps.addCourierReturnsSuccessResponse(login, password);
        isCourierCreated=true;
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

        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс после логина
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравним статус код
        CreateCourierSteps.checkStatusCode(response, 200);
        //вытянем courierID
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);
        isCourierCreated = true;
        assertNotNull(courierId, "ID не должен быть NULL");
        assertTrue(courierId > 0, "Id должен быть больше 0");
    }



    @Test
    @DisplayName("Login with invalid password")
    public void loginWithWrongPasswordReturnsNotFoundError() {

        //залогинемся с валидными данными чтобы получить id
        //preconditions
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс после попытки логина
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);

        //Сам тест
        //сгенерируем новый пароль
        String wrongPassword = DataGenerator.generateCourierPassword();
        //залогинемся с неправильным паролем
        CourierCredentials courierCredentialsWrong = new CourierCredentials(login, wrongPassword);
        Response responseAfterLogin = CreateCourierSteps.loginWithLoginAndPassword(courierCredentialsWrong);
        CreateCourierSteps.checkStatusCode(responseAfterLogin,404);
        CreateCourierSteps.checkResponseValue(responseAfterLogin, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Login without password")
    public void loginWithoutPasswordReturnsBadRequestError() {
        //preconditions
        //залогинемся чтобы получить верный id
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс
        Response response = CreateCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = CreateCourierSteps.getCourierIdAfterSuccessLogin(response);
        //сам тест
        //login without  password
        CourierCredentials courierCredentialsNoPassword = new CourierCredentials(login, null);
        Response responseAfterLogin = CreateCourierSteps.loginWithLoginAndPassword(courierCredentialsNoPassword);
        CreateCourierSteps.checkStatusCode(responseAfterLogin,400);
        CreateCourierSteps.checkResponseValue(responseAfterLogin, "message", "Недостаточно данных для входа");

    }


}
