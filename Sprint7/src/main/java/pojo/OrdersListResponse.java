package pojo;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class OrdersListResponse {
    private List <OrderResponse> orders;


    public OrdersListResponse() {}
    public OrdersListResponse(List <OrderResponse> orders) {
        this.orders = orders;
    }

}
