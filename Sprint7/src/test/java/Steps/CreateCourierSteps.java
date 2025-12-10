package Steps;

import POJO.Courier;
import POJO.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.given;

public class CreateCourierSteps {


    @Step("Создадим курьера ")
    public static Response createCourier(Courier courier) {

        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
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

/*
    @Step("Получим id созданного курьера")
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
    */


    //попробую разделить шаги пост запрос с логином и валидация ответа
    @Step("логин в систему")
    public static Response loginWithLoginAndPassword(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("получение id после успешного логина")
    public static Integer getCourierIdAfterSuccessLogin(Response response) {
        return response.path("id");
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


    @Step("Просто успешное добавление курьера (без айди)")
    public static Response addCourierReturnsSuccessResponse(String login, String password) {
            Courier courier = new Courier(login, password, null);
            Response response = createCourier(courier);
            checkStatusCode(response, 201);
            return response;
    }

    @Step("более высокоуровненвый - создает курьера, убеждается, что создался и затем логинимся чтобы получить id")
    public static Integer addCourierAndGetCourierId(String login, String password) {
        Courier courier = new Courier(login, password, null);
        Response response = createCourier(courier);
        checkStatusCode(response, 201);
        CourierCredentials courierCredentials = new CourierCredentials(login,password);
        Response responseAfterLogin = loginWithLoginAndPassword(courierCredentials);
        return getCourierIdAfterSuccessLogin(responseAfterLogin);
    }

/*
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
    }*/

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
        /*
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
    }*/
    /*
    @Step("Удаляем курьера по ID {id}")
    private void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(courierId))
                .then()
                .statusCode(200);

    }*/
}
