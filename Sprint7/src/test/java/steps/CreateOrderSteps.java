package steps;

import pojo.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderSteps {

    //проверим правильно создание объекта
    @Step("Проверяем правильное создание объекта")
    public void validateOrderFields(Order order,//убрала static
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
    @Step("Отправление запрос на создание заказа")
    public  Response sendPostRequestToCreateOrder(Order order) {//static removed
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
    @Step("Проверяем код сообщения response - {expectedCode}")
    public  void checkStatusCode(Response response, int expectedCode) {//static removed
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }
    @Step("Проверяем наличие track number")
    public  void checkTrackExists(Response response) {//static removed
        Integer track = response.jsonPath().getInt("track"); // получаем как Integer
        assertNotNull(track, "Track отсутствует в ответе"); // проверяем, что поле есть
        assertTrue(track > 0, "Track должен быть положительным числом"); // опционально, что число > 0
    }

    @Step("Получаем track number")//static removed
    public  Integer getOrderTrackAfterSuccessAddition(Response response) {
        return response.path("track");
    }

    //так как сейчас нет цели проверять нормальную работу отмены заказа, в более подробные проверки
    //здесь я не лезу
    @Step("Отменяем заказ после создания")
    public  void cancelOrderAfterSuccessCreation(int track) {//static removed
        given()
                .header("Content-type", "application/json")
                .queryParam("track", track)
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .statusCode(200);
    }


}
