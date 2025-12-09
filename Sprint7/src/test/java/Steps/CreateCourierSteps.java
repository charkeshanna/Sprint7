package Steps;

import POJO.Courier;
import POJO.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;


import static io.restassured.RestAssured.given;

public class CreateCourierSteps {
    public String generatedCourierLogin;
    public String generatedCourierPassword;
    private boolean isCourierCreated = false;

    @Step("Создадим курьера ")
    public static Response createCourier(Courier courier) {

        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract()
                .response();

    }

    @Step("проверим статус код = {expectedCode}")
    public static void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }

    @Step("проверим тело сообщения {expectedValue}")
    public static void checkResponseValue(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertEquals(expectedValue, actualValue, "Значение по ключу '" + key + "' не совпадает");
    }

    @Step("Получим созданного курьера")
    public static Integer getCourierId(CourierCredentials courierCredentials) {
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

    @Step("Удаление курьера по courierId")
    public static void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(id))
                .then()
                .statusCode(200);

    }


    /*@Step("Генерируем пару логин - пароль")
    public void generateCredentials() {
        generatedCourierLogin = "login_" + System.currentTimeMillis();
        generatedCourierPassword = "password" + (int)(Math.random() * 10000);
    }

    @Step("Send POST request to /api/v1/courier -- создание курьера")
    public Response sendPostRequestToCreateCourier() {
        generateCredentials();
        Courier courier = new Courier(generatedCourierLogin, generatedCourierPassword, "Name" + generatedCourierLogin);
        Response response =  given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Получаем id созданного курьера")
    private int getCourierId() {
        return 0;
    }

    @Step("Удаление созданного курьера")
    private void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(courierId))
                .then()
                .statusCode(200);

    }

     */
}
