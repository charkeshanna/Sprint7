package pojo;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Setter
@Getter
public class OrderResponse {
    //определяю все поля
    private Integer id;
    private Integer courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private Integer track;
    private List<String> color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private Integer status;


    //пустой конструктор
    OrderResponse () {}
    //конструктор с параметрами
    OrderResponse (Integer id, Integer courierId, String firstName, String lastName, String address,
                   String metroStation, String phone, Integer rentTime, String deliveryDate, Integer track,
                   List<String> color, String comment, String createdAt, String updatedAt,
                   Integer status) {
        this.id=id;
        this.courierId = courierId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.track = track;
        this.color =color;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
