package Tests;

import POJO.Courier;
import POJO.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class authenticationTest {
    private String generatedCourierLogin;
    private String generatedCourierPassword;
    private boolean isCourierCreated = false;
    private int courierId;
    private String generatedAnyPassword;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @AfterEach
    public void deleteCourier() {
        if (courierId != 0 && isCourierCreated) {
            deleteCourierById(courierId);
        }
    }

    @Step("Генерируем случайный пароль, не связанный с логином")
    private void generateAnyPassword() {
        generatedAnyPassword = (int)(Math.random() * 10000) + "wrongPassword";
    }

    @Step("Создадим курьера ")
    private void createCourier() {
        generatedCourierLogin = "login_" + System.currentTimeMillis();
        generatedCourierPassword = "password" + (int)(Math.random() * 10000);

        Courier courier = new Courier(generatedCourierLogin, generatedCourierPassword, "Test Courier");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        isCourierCreated = true;
    }

    @Step("Логин с верным логином и паролем")
    private int loginCourier(String login, String password) {
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Удаляем курьера по ID {id}")
    private void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(courierId))
                .then()
                .statusCode(200);

    }






    @Test
    @DisplayName("Authentication with valid login and password")
    public void loginWithValidCredentialsReturnsIdAndSuccessResponse() {
        //create courier
        createCourier();
        //получаем id после успешного логина
        courierId = loginCourier(generatedCourierLogin, generatedCourierPassword);

        assertNotNull(courierId, "ID не должен быть NULL");
        assertTrue(courierId > 0, "Id должен быть больше 0");
    }

    @Test
    @DisplayName("Login with not existing login")
    public void loginWithWrongLoginReturnsNotFoundError() {
        String randomLogin = "non_existing_" + System.currentTimeMillis();
        String randomPassword = "anyPassword";
        CourierCredentials courierCredentials = new CourierCredentials(randomLogin, randomPassword);
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
        createCourier();
        //получили верный id при успешном логине
        courierId = loginCourier(generatedCourierLogin, generatedCourierPassword);
        //сгенерируем неправильный пароль
        generateAnyPassword();

        //залогинимся с неправильным паролем
        CourierCredentials courierCredentialsWrong = new CourierCredentials(generatedCourierLogin, generatedAnyPassword);

        given()
                .header("Content-type", "application/json")
                .body(courierCredentialsWrong)
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
        createCourier();
        //получим id при успешном логине
        courierId = loginCourier(generatedCourierLogin, generatedCourierPassword);

        //login without  password
        CourierCredentials courierCredentials = new CourierCredentials(generatedCourierLogin, null);

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
        generateAnyPassword();
        //login without login
        CourierCredentials courierCredentials = new CourierCredentials(null, generatedAnyPassword);

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
