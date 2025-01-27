package ru.netology.testing_data_API;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testing_data_API.UserDataGenerator.Registration.getRegisteredUser;
import static ru.netology.testing_data_API.UserDataGenerator.Registration.getUser;
import static ru.netology.testing_data_API.UserDataGenerator.getRandomLogin;
import static ru.netology.testing_data_API.UserDataGenerator.getRandomPassword;

class AuthTest {
    SelenideElement loginField = $x("//*[@data-test-id='login']//input");
    SelenideElement passwordField = $x("//*[@data-test-id='password']//input");
    SelenideElement submitButton = $x("//button[@data-test-id='action-login']");


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        loginField.setValue(registeredUser.getLogin());
        passwordField.setValue(registeredUser.getPassword());
        submitButton.click();
        $x("//*[contains(text(), 'Личный кабинет')]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        loginField.setValue(notRegisteredUser.getLogin());
        passwordField.setValue(notRegisteredUser.getPassword());
        submitButton.click();
        $x("//*[@data-test-id='error-notification']").shouldHave(Condition.text("Неверно указан логин или пароль")).
                shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        loginField.setValue(blockedUser.getLogin());
        passwordField.setValue(blockedUser.getPassword());
        submitButton.click();
        $x("//*[@data-test-id='error-notification']").shouldHave(Condition.text("Пользователь заблокирован")).
                shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        loginField.setValue(wrongLogin);
        passwordField.setValue(registeredUser.getPassword());
        submitButton.click();
        $x("//*[@data-test-id='error-notification']").shouldHave(Condition.text("Неверно указан логин или пароль")).
                shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        loginField.setValue(registeredUser.getLogin());
        passwordField.setValue(wrongPassword);
        submitButton.click();
        $x("//*[@data-test-id='error-notification']").shouldHave(Condition.text("Неверно указан логин или пароль")).
                shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
