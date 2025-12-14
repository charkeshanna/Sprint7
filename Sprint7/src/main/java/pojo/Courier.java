package pojo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Courier {

    private String login;
    private String password;
    private String firstName;

    public Courier() {}
    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }



}
