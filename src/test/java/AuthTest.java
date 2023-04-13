import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    //вход в личный кабинет с учётными данными зарегистрированного активного пользователя registeredUser
    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $x("//span[@data-test-id = 'login'] //input").setValue(registeredUser.getLogin());
        $x("//span[@data-test-id = 'password'] //input").setValue(registeredUser.getPassword());
        $x("//span[@class ='button__text']").click();
        $x("//h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    //вход в личный кабинет с учётными данными незарегистрированного активного пользователя notRegisteredUser
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $x("//span[@data-test-id = 'login'] //input").setValue(notRegisteredUser.getLogin());
        $x("//span[@data-test-id = 'password'] //input").setValue(notRegisteredUser.getPassword());
        $x("//span[@class ='button__text']").click();
        $x("//div [@class = 'notification__content']")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    // попытка входа в личный кабинет с учётными данными заблокированного пользователя blockedUser
    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//span[@data-test-id = 'login'] //input").setValue(blockedUser.getLogin());
        $x("//span[@data-test-id = 'password'] //input").setValue(blockedUser.getPassword());
        $x("//span[@class ='button__text']").click();
        $x("//div [@class = 'notification__content']")
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    //вход в личный кабинет с неверным логином wrongLogin и паролем зарегистрированного пользователя registeredUser
    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $x("//span[@data-test-id = 'login'] //input").setValue(wrongLogin);
        $x("//span[@data-test-id = 'password'] //input").setValue(registeredUser.getPassword());
        $x("//span[@class ='button__text']").click();
        $x("//div [@class = 'notification__content']")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
    //вход в личный кабинет с логином зарегистрированного пользователя registeredUser и неправильным паролем wrongPassword
    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $x("//span[@data-test-id = 'login'] //input").setValue(registeredUser.getLogin());
        $x("//span[@data-test-id = 'password'] //input").setValue(wrongPassword);
        $x("//span[@class ='button__text']").click();
        $x("//div [@class = 'notification__content']")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
}