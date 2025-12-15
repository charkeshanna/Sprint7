package tests;

import pojo.Courier;
import utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CreateCourierSteps;

public class CreateCourierTestWithoutLoginOrPasswordTest extends BaseTest{
    private String login;
    private String password;
    CreateCourierSteps createCourierSteps;
    @BeforeEach
    public void setUp() {
        createCourierSteps = new CreateCourierSteps();
    }


    @Test
    @DisplayName("Create courier without login")
    public void addCourierWithoutLoginReturnsBadRequest() {
        //генерю данные
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(null, password, null);
        Response response = createCourierSteps.createCourier(courier);
        //проверяю ответ
        createCourierSteps.checkStatusCode( response, 400);
        createCourierSteps.checkResponseValue(response, "message", "Недостаточно данных для создания учетной записи");

    }

    @Test
    @DisplayName("Create courier without password")
    public void addCourierWithoutPasswordReturnsBadRequest(){
        //генерю данные
        login = DataGenerator.generateCourierLogin();
        // password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, null, null);
        Response response = createCourierSteps.createCourier(courier);
        //проверяю ответ
        createCourierSteps.checkStatusCode( response, 400);
        createCourierSteps.checkResponseValue(response, "message", "Недостаточно данных для создания учетной записи");

    }
}
