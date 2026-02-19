package bank;

import bank.data.RegistrationDto;
import bank.data.UserGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class AuthApiTest {

    @BeforeAll
    static void setUpAll() {
    }

    @Test
    void shouldCreateActiveUser() {
        RegistrationDto user = UserGenerator.generateActiveUser("ru");

        given()
                .baseUri("http://localhost")
                .port(9999)
                .contentType("application/json")
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldCreateBlockedUser() {
        RegistrationDto user = UserGenerator.generateBlockedUser("ru");

        given()
                .baseUri("http://localhost")
                .port(9999)
                .contentType("application/json")
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldOverwriteExistingUser() {
        // Создаём пользователя 2 раза
        RegistrationDto user1 = UserGenerator.generateActiveUser("ru");
        ApiClient.createUser(user1);

        RegistrationDto user2 = new RegistrationDto(
                user1.getLogin(),
                "newpassword",
                "blocked"
        );
        ApiClient.createUser(user2);
    }
}