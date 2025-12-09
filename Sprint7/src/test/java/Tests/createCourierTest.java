package Tests;
import POJO.CourierCredentials;
import Steps.CreateCourierSteps;
import POJO.Courier;
import Utils.DataGenerator;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static Steps.CreateCourierSteps.createCourier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class createCourierTest {

    //private String generatedCourierLogin;
   // private String generatedCourierPassword;
    private String login;
    private String password;
    private boolean isCourierCreated = false;
    private int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @AfterEach
    public void tearDown() {
        if(courierId!=0 && isCourierCreated) {
            CreateCourierSteps.deleteCourierById(courierId);
        }
    }



    @Test
    @DisplayName("Check correct response code and text after adding a courier with all fields filled")
    @Description("Basic test for /api/v1/courier endpoint")
    public void createCourierWithRequiredAndOptionalFieldsReturnsSuccessResponse() {
        //генерю данные
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);
        //меняю значение переменной, что курьер был создан
        isCourierCreated = true;
        //авторизация для получения courierId
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        courierId = CreateCourierSteps.getCourierId(courierCredentials);
    }

    //
    @Test
    public void createCourierWithOnlyRequiredFields() {
        //генерю данные
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, null);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);
        //меняю значение переменное что курьер был создан
        isCourierCreated = true;
        //авторизация для получения courierId
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        courierId = CreateCourierSteps.getCourierId(courierCredentials);
    }

    @Test
    @DisplayName("Second creation of the courier with the same login")
    @Description("Trying to add a courier with the same login as an already added one")
    public void addCourierWithTheSameLoginReturnsConflictResponse(){
        //генерю данные
        login = DataGenerator.generatedCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);
        //меняю значение переменной, что курьер был создан
        isCourierCreated = true;
        //авторизация для получения courierId
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        courierId = CreateCourierSteps.getCourierId(courierCredentials);

        //send the same data for the 2nd time
        //создаю курьера с той же парой логин - пароль
        Courier courierSecond = new Courier(login, password, "firstname" + login);
        Response responseSecond = CreateCourierSteps.createCourier(courierSecond);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( responseSecond, 409);
        CreateCourierSteps.checkResponseValue(responseSecond, "message", "Этот логин уже используется");

    }


    @Test
    @DisplayName("Create courier without login")
    public void addCourierWithoutLoginReturnsBadRequest() {
        //генерю данные
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(null, password, null);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 400);
        CreateCourierSteps.checkResponseValue(response, "message", "Недостаточно данных для создания учетной записи");

    }

    @Test
    @DisplayName("Create courier without password")
    public void addCourierWithoutPasswordReturnsBadRequest(){
        //генерю данные
        login = DataGenerator.generatedCourierLogin();
       // password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, null, null);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 400);
        CreateCourierSteps.checkResponseValue(response, "message", "Недостаточно данных для создания учетной записи");

    }
}
