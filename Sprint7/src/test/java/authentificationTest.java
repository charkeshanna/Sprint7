import POJO.Courier;
import POJO.CourierCredentials;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class authentificationTest {
    private String generatedCourierLogin;
    private String generatedCourierPassword;
    private String finalCourierPassword;
    private int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    //сгенерим данные для логина
    private void generateCredentials() {
        generatedCourierLogin = "login_" + System.currentTimeMillis();
        generatedCourierPassword = "password" + (int)(Math.random() * 10000);
    }

    //создадим курьера
    private void createCourier(String password) {
        generateCredentials();


        if (password!=null) {
            finalCourierPassword = password;
        } else {
            finalCourierPassword = generatedCourierPassword;
        }

        Courier courier = new Courier(generatedCourierLogin, finalCourierPassword, "Test Courier");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @AfterEach
    //написать метод удаления курьера
    public void deleteCourier() {
        if (courierId != 0) {
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/" + String.valueOf(courierId))
                    .then()
                    .statusCode(200);
        }
    }
    @Test
    @DisplayName("Authentication with valid login and password")

    public void loginWithValidLoginAndPasswordReturnsIdAndSuccessResponse() {
        //create courier
        createCourier(null);

        //check successful login with valid login and password
        CourierCredentials courierCredentials = new CourierCredentials(generatedCourierLogin, finalCourierPassword);
        courierId = given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotNull(courierId, "ID не должен быть NULL");
        assertTrue(courierId > 0, "Id должен быть больше 0");
    }

    @Test
    @DisplayName("Login with not existing login")
    public void loginWithWrongLoginReturnsNotFoundError() {
        generateCredentials();
        CourierCredentials courierCredentials = new CourierCredentials(generatedCourierLogin, generatedCourierPassword);
        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login with invalid password")
    public void loginWithWrongPasswordReturnsNotFoundError() {
        //create new courier
        createCourier(null);

        //login with wrong password
        CourierCredentials courierCredentials = new CourierCredentials(generatedCourierPassword, generatedCourierPassword);

        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login without password")
    public void loginWithoutPasswordReturnsBadRequestError() {
        //create new courier
        createCourier(null);

        //login without  password
        CourierCredentials courierCredentials = new CourierCredentials(generatedCourierPassword, null);

        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)//Проверить еще раз - падает 504 ошибка
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login without login")
    public void loginWithoutLoginReturnsBadRequestError() {

        //login without login
        CourierCredentials courierCredentials = new CourierCredentials(null, "sdf3566");

        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
