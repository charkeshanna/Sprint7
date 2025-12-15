package utils;
import com.github.javafaker.Faker;


public class DataGenerator {
    private static final Faker faker = new Faker();

    public static String generateCourierLogin() {
        return faker.name().username();
    }

    public static String generateCourierPassword() {
        return faker.internet().password(8, 12);
    }

}
