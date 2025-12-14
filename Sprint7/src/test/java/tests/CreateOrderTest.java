package tests;

import pojo.Order;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import steps.CreateOrderSteps;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

public class CreateOrderTest {
    Integer orderTrackNumber;
    boolean isOrderCreated = false;
    CreateOrderSteps createOrderSteps;
    @BeforeEach
    public void setUp() {
        createOrderSteps = new CreateOrderSteps();
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @AfterEach
    public void tearDown() {
        if(orderTrackNumber!=0 && isOrderCreated) {
           createOrderSteps.cancelOrderAfterSuccessCreation(orderTrackNumber);
        }
    }
    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Check that order successfully added")
    @Description("Check that order is added with different data")
    void checkSuccessOrderCreation(String firstName, String lastName,
                                   String address, String metroStation,
                                   String phone, int rentTime,
                                   String deliveryDate, String comment,
                                   List<String> color){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        //проверю что поля в объекте создались нормально
        createOrderSteps.validateOrderFields(order, firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        //отправлю запрос
        Response response = createOrderSteps.sendPostRequestToCreateOrder(order);
        //проверим код сообщения
        createOrderSteps.checkStatusCode(response, 201);
        //проверим тело сообщения, что возвращает track
        createOrderSteps.checkTrackExists(response);
        //получим track number
        orderTrackNumber = createOrderSteps.getOrderTrackAfterSuccessAddition(response);
        isOrderCreated = true;

    }
    //test data
    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("FirstNameTest",
                        "LastNameTest", "Any Address", "Сокольники", "79454874560",
                        2, "2025-12-31", "Some comments", List.of("GREY")),
                Arguments.of("FirstNameTest2",
                        "LastNameTest2", "Any Address2", "Лубянка", "79454874569",
                        2, "2025-12-27", "Some comments2", List.of("Black", "Gray")),
                Arguments.of("FirstNameTest3",
                        "LastNameTest3", "Any Address3", "Чистые Пруды", "79454874569",
                        2, "2025-12-27", null, List.of("Black")),
                Arguments.of("FirstNameTest3",
                        "LastNameTest3", "Any Address3", "Чистые Пруды", "79454874569",
                        2, "2025-12-27", null, null)
        );
    }
}
