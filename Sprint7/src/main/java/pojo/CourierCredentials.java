package pojo;

public class CourierCredentials {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public CourierCredentials() {}
    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
