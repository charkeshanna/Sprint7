package pojo;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter

public class CourierCredentials {
    private String login;
    private String password;


    public CourierCredentials() {}
    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
