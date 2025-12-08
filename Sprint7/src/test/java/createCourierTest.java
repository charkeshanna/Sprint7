import POJO.Courier;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class createCourierTest {
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check correct response code and text after adding a courier will all fields filled")
    @Description("Basic test for /api/v1/courier endpoint")
    public void addCourierWithAllFieldsReturns201Response() {
        Courier courier = new Courier("CharkesTest3", "123456", "CharkesTest3");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Check correct response code and text after adding a courier will only required fields filled")
    @Description("Basic test for /api/v1/courier endpoint")
    public void addCourierWithOnlyRequiredFieldsReturns201Response() {
        String json = "{\"login\": \"OnlyLogAndPass1\", \"password\":\"6755anypassw\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Second creation of the courier with the same login")
    @Description("Trying to add a courier with the same login as an already added one")
    public void addCourierWithTheSameLoginReturnsConflictResponse(){
        Courier courier = new Courier("DuplicatedCharkes2", "123456", "DuplicatedCharkes2");
        //create courier for the 1st time
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));

        //send the same data for the 2nd time
        Courier courier1 = new Courier("DuplicatedCharkes2", "98765", "CharkesDuplicate2");
        given()
                .header("Content-type", "application/json")
                .body(courier1)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Create courier without login")
    public void addCourierWithoutLoginReturnsBadRequest() {
        String json = "{\"password\": \"2435\", \"firstName\":\"NoLoginCourier\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create courier without password")
    public void addCourierWithoutPasswordReturnsBadRequest(){
        String json = "{\"login\": \"NoPasswordCourier\", \"firstName\":\"NoLoginCourier\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
}
