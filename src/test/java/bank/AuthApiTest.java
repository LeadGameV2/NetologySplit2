package bank;

import bank.data.RegistrationDto;
import bank.data.UserGenerator;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

class AuthApiTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = UserGenerator.generateUserWithStatus("ru", "active");
        ApiClient.createUser(registeredUser);

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("h2.heading")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = UserGenerator.generateUserWithStatus("ru", "active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = UserGenerator.generateUserWithStatus("ru", "blocked");
        ApiClient.createUser(blockedUser);

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = UserGenerator.generateUserWithStatus("ru", "active");
        ApiClient.createUser(registeredUser);

        // Фиксированный неправильный логин
        var wrongLogin = "логин123";

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = UserGenerator.generateUserWithStatus("ru", "active");
        ApiClient.createUser(registeredUser);

        // Фиксированный неправильный пароль
        var wrongPassword = "пароль123";

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }
}
