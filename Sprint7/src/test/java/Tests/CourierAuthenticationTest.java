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
    CreateCourierSteps createCourierSteps;


    @BeforeEach
    public void setUp() {
        createCourierSteps = new CreateCourierSteps ();
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        //сгенерируем логин и пароль
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создадим курьера
        createCourierSteps.addCourierReturnsSuccessResponse(login, password);
        isCourierCreated=true;
    }


    @AfterEach
    public void deleteCourier() {
        if (courierId != 0 && isCourierCreated) {
            createCourierSteps.deleteCourierById(courierId);
        }
    }


    @Test
    @DisplayName("Authentication with valid login and password")
    public void loginWithValidCredentialsReturnsIdAndSuccessResponse() {

        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс после логина
        Response response = createCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //сравним статус код
        createCourierSteps.checkStatusCode(response, 200);
        //вытянем courierID
        courierId = createCourierSteps.getCourierIdAfterSuccessLogin(response);
        isCourierCreated = true;
        assertNotNull(courierId, "ID не должен быть NULL");
        assertTrue(courierId > 0, "Id должен быть больше 0");
    }



    @Test
    @DisplayName("Authentication with invalid password")
    public void loginWithWrongPasswordReturnsNotFoundError() {

        //залогинемся с валидными данными чтобы получить id
        //preconditions
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс после попытки логина
        Response response = createCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = createCourierSteps.getCourierIdAfterSuccessLogin(response);

        //Сам тест
        //сгенерируем новый пароль
        String wrongPassword = DataGenerator.generateCourierPassword();
        //залогинемся с неправильным паролем
        CourierCredentials courierCredentialsWrong = new CourierCredentials(login, wrongPassword);
        Response responseAfterLogin = createCourierSteps.loginWithLoginAndPassword(courierCredentialsWrong);
        createCourierSteps.checkStatusCode(responseAfterLogin,404);
        createCourierSteps.checkResponseValue(responseAfterLogin, "message", "Учетная запись не найдена");

    }

    @Test
    @DisplayName("Authentication without password")
    public void loginWithoutPasswordReturnsBadRequestError() {
        //preconditions
        //залогинемся чтобы получить верный id
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        //получим респонс
        Response response = createCourierSteps.loginWithLoginAndPassword(courierCredentials);
        //получим id
        courierId = createCourierSteps.getCourierIdAfterSuccessLogin(response);
        //сам тест
        //login without  password
        CourierCredentials courierCredentialsNoPassword = new CourierCredentials(login, null);
        Response responseAfterLogin = createCourierSteps.loginWithLoginAndPassword(courierCredentialsNoPassword);
        createCourierSteps.checkStatusCode(responseAfterLogin,400);
        createCourierSteps.checkResponseValue(responseAfterLogin, "message", "Недостаточно данных для входа");

    }


}
