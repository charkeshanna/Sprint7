package Tests;

import POJO.Order;
import POJO.OrderResponse;
import POJO.OrdersListResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrdersListTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public  void getOrdersList() {
        OrdersListResponse ordersListResponse = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders")
                .body().as(OrdersListResponse.class);
        assertNotNull(ordersListResponse.getOrders(), "Список заказов пустой");
        OrderResponse firstOrder = ordersListResponse.getOrders().get(0);
        assertNotNull(firstOrder.getId(), "ID заказа отсутствует");
        assertTrue(firstOrder.getId() > 0, "ID заказа неправильный");
        assertNotNull(firstOrder.getStatus(), "Статус заказа отсутствует");







/*
            Response response = given()
                .get("/api/v1/orders");
            response.then()
                    //.log()
                    //.all()
                    .statusCode(200);
            OrdersListResponse ordersList = response.then().extract().as(OrdersListResponse.class);
            assert(ordersList.getOrders().size() > 0);
        System.out.println("=== POJO OBJECT ===");
        System.out.println(ordersList.toString());
        */

    }
}
