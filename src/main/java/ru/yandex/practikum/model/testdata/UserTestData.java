package ru.yandex.practikum.model.testdata;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practikum.model.user.User;

import java.util.Locale;
import java.util.Random;

public class UserTestData {
    private final static Faker faker = new Faker(Locale.forLanguageTag("ru"));

    public static String rndName() {
        return faker.name().fullName();
    }
    public static String rndPassword() {
        return RandomStringUtils.randomAlphanumeric(8, 13) + "$";
    }
    public static String rndEmail() {
        return String.format("%s@%s.ru", RandomStringUtils.randomAlphanumeric(5, 13), RandomStringUtils.randomAlphanumeric(5, 13)).toLowerCase();
    }

    public static User newUser() {
        return new User(rndName(), rndEmail(), rndPassword());
    }

    public static String getRndString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();
        while (result.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            result.append(SALTCHARS.charAt(index));
        }
        return result.toString();
    }
}
