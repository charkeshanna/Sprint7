package steps;
import pojo.OrderResponse;
import pojo.OrdersListResponse;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Get orders list method")
    public OrdersListResponse getOrdersList() {
        return given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders")
                .body().as(OrdersListResponse.class);
    }

    @Step("Get first order from the given orders list")
    public OrderResponse getFirstOrder(OrdersListResponse ordersListResponse) {
        return ordersListResponse.getOrders().get(0);
    }

}
