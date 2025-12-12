package Tests;
import POJO.CourierCredentials;
import steps.CreateCourierSteps;
import POJO.Courier;
import Utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CreateCourierTest {

    private String login;
    private String password;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }



    @AfterEach
    public void tearDown() {
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        int courierId = CreateCourierSteps.getCourierIDafterSuccessLogin(courierCredentials);
        if (courierId != 0) {
            CreateCourierSteps.deleteCourierById(courierId);
        }
    }



    @Test
    @DisplayName("Check correct response code and text after adding a courier with all fields filled")
    @Description("Basic test for /api/v1/courier endpoint")
    public void createCourierWithRequiredAndOptionalFieldsReturnsSuccessResponse() {
        //генерирую логин и пароль
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);

    }

    //
    @Test
    public void createCourierWithOnlyRequiredFields() {
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, null);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);


    }

    @Test
    @DisplayName("Second creation of the courier with the same login")
    @Description("Trying to add a courier with the same login as an already added one")
    public void addCourierWithTheSameLoginReturnsConflictResponse(){
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = CreateCourierSteps.createCourier(courier);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( response, 201);
        CreateCourierSteps.checkResponseValue(response, "ok", true);



        //send the same data for the 2nd time
        //создаю курьера с той же парой логин - пароль
        Courier courierSecond = new Courier(login, password, "firstname" + login);
        Response responseSecond = CreateCourierSteps.createCourier(courierSecond);
        //проверяю ответ
        CreateCourierSteps.checkStatusCode( responseSecond, 409);
        CreateCourierSteps.checkResponseValue(responseSecond, "message", "Этот логин уже используется");

    }
}
