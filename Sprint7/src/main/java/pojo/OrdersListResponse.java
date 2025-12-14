package pojo;

import java.util.List;

public class OrdersListResponse {
    private List <OrderResponse> orders;

    public List<OrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderResponse> orders) {
        this.orders = orders;
    }


    public OrdersListResponse() {}
    public OrdersListResponse(List <OrderResponse> orders) {
        this.orders = orders;
    }

}
