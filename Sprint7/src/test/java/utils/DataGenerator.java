package utils;

public class DataGenerator {
    public static String generateCourierLogin() {
        return "login_" + System.currentTimeMillis();
    }

    public static String generateCourierPassword() {
        return "password" + (int)(Math.random() * 10000);
    }
}
