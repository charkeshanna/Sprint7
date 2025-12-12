package Steps;

import POJO.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderSteps {
    Order order;
    //проверим правильно создание объекта
    @Step("проверим правильно создание объекта")
    public static void validateOrderFields(Order order,
                                           String firstName, String lastName,
                                           String address, String metroStation,
                                           String phone, int rentTime,
                                           String deliveryDate, String comment,
                                           List<String> color) {
        assertEquals(firstName, order.getFirstName());
        assertEquals(lastName, order.getLastName());
        assertEquals(address, order.getAddress());
        assertEquals(metroStation, order.getMetroStation());
        assertEquals(phone, order.getPhone());
        assertEquals(rentTime, order.getRentTime());
        assertEquals(deliveryDate, order.getDeliveryDate());
        assertEquals(comment, order.getComment());
        assertEquals(color, order.getColor());
    }
    @Step("Отправим запрос на создание заказа")
    public static Response sendPostRequestToCreateOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
    @Step("Проверим код сообщения response - {expectedCode}")
    public static void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }
    @Step("Проверим наличие track number")
    public static void checkTrackExists(Response response) {
        Integer track = response.jsonPath().getInt("track"); // получаем как Integer
        assertNotNull(track, "Track отсутствует в ответе"); // проверяем, что поле есть
        assertTrue(track > 0, "Track должен быть положительным числом"); // опционально, что число > 0
    }

    @Step("Получим track number")
    public static Integer getOrderTrackAfterSuccessAddition(Response response) {
        return response.path("track");
    }

    @Step("отменим заказ после создания")
    public static void cancelOrderAfterSuccessCreation(int track) {
        given()
                .header("Content-type", "application/json")
                .queryParam("track", track)
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .statusCode(200);
    }


}
