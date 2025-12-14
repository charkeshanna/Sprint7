package tests;
import pojo.OrderResponse;
import pojo.OrdersListResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrdersListTest extends BaseTest{



    @Test
    @DisplayName("Check that sending GET request to /api/v1/orders return orders list")
    public  void getOrdersList() {
        //так как задание не было четко сформулировано, я решила сделать только основной тест, что отправление
        //запроса возвращает список
        OrdersListResponse ordersListResponse = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders")
                .body().as(OrdersListResponse.class);
        assertNotNull(ordersListResponse.getOrders(), "Список заказов пустой");
        OrderResponse firstOrder = ordersListResponse.getOrders().get(0);
        assertNotNull(firstOrder.getId(), "ID заказа отсутствует");
        assertTrue(firstOrder.getId() > 0, "ID заказа неправильный");
        assertNotNull(firstOrder.getStatus(), "Статус заказа отсутствует");


    }
}
