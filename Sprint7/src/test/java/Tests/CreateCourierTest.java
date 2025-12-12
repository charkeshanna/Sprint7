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
    CreateCourierSteps createCourierSteps;

    @BeforeEach
    public void setUp() {
        createCourierSteps = new CreateCourierSteps();
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }



    @AfterEach
    public void tearDown() {
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        int courierId = createCourierSteps.getCourierIDafterSuccessLogin(courierCredentials);
        if (courierId != 0) {
            createCourierSteps.deleteCourierById(courierId);
        }
    }



    @Test
    @DisplayName("Check correct response code and text after adding a courier with all fields filled")
    public void createCourierWithRequiredAndOptionalFieldsReturnsSuccessResponse() {
        //генерирую логин и пароль
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = createCourierSteps.createCourier(courier);
        //проверяю ответ
        createCourierSteps.checkStatusCode( response, 201);
        createCourierSteps.checkResponseValue(response, "ok", true);

    }


    @Test
    @DisplayName("Check correct response code and text after adding a courier with only required fields filled")
    public void createCourierWithOnlyRequiredFields() {
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, null);
        Response response = createCourierSteps.createCourier(courier);
        //проверяю ответ
        createCourierSteps.checkStatusCode( response, 201);
        createCourierSteps.checkResponseValue(response, "ok", true);


    }

    @Test
    @DisplayName("Second creation of the courier with the same login")
    public void addCourierWithTheSameLoginReturnsConflictResponse(){
        login = DataGenerator.generateCourierLogin();
        password = DataGenerator.generateCourierPassword();
        //создаю курьера
        Courier courier = new Courier(login, password, "firstname" + login);
        Response response = createCourierSteps.createCourier(courier);
        //проверяю ответ
        createCourierSteps.checkStatusCode( response, 201);
        createCourierSteps.checkResponseValue(response, "ok", true);



        //send the same data for the 2nd time
        //создаю курьера с той же парой логин - пароль
        Courier courierSecond = new Courier(login, password, "firstname" + login);
        Response responseSecond = createCourierSteps.createCourier(courierSecond);
        //проверяю ответ
        createCourierSteps.checkStatusCode( responseSecond, 409);
        createCourierSteps.checkResponseValue(responseSecond, "message", "Этот логин уже используется");

    }
}
