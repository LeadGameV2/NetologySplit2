package bank.data;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class UserGenerator {

    public RegistrationDto generateUserWithStatus(String locale, String status) {
        Faker faker = new Faker(new Locale(locale));
        return new RegistrationDto(
                faker.name().username(),
                faker.internet().password(6, 10),
                status  // теперь статус передаётся параметром
        );
    }
}