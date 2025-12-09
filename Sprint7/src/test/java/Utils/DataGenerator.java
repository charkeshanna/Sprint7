package Utils;

public class DataGenerator {
    public static String generatedCourierLogin() {
        return "login_" + System.currentTimeMillis();
    }

    public static String generateCourierPassword() {
        return "password" + (int)(Math.random() * 10000);
    }
}
