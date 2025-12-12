package steps;

import POJO.Courier;
import POJO.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.given;

public class CreateCourierSteps {


    @Step("Создаем курьера, отправляя запрос")
    public static Response createCourier(Courier courier) {

        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Проверяем статус код = {expectedCode} полученного ответа")
    public static void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }

    @Step("Проверяем тело сообщения {expectedValue} полученного ответа")
    public static void checkResponseValue(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertEquals(expectedValue, actualValue, "Значение по ключу '" + key + "' не совпадает");
    }

    @Step("Логинимся и получаем courierID")
    public static int getCourierIDafterSuccessLogin(CourierCredentials courierCredentials) {
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


    //попробую разделить шаги пост запрос с логином и валидация ответа
    @Step("Логин в систему без обработки ответа")
    public static Response loginWithLoginAndPassword(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Получаем id после успешного логина (обработка ответа)")
    public static Integer getCourierIdAfterSuccessLogin(Response response) {
        return response.path("id");
    }

    @Step("Удаляем курьера по courierId {id}")
    public static void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(id))
                .then()
                .statusCode(200);

    }
    @Step("Успешное добавление курьера (без получения айди)")
    public static Response addCourierReturnsSuccessResponse(String login, String password) {
        Courier courier = new Courier(login, password, null);
        Response response = createCourier(courier);
        checkStatusCode(response, 201);
        return response;
    }


}
