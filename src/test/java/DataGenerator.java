import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;


public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    // Отправляем запрос на указанный в требованиях path, передав в body запроса объект user и подготовленную спецификацию requestSpec.
    private static void sendRequest(RegistrationDto user) { //метод принимает параметром какого-то пользователя
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                    .post("/api/system/users")
                .then()
                .statusCode(200);
    }
    //добавляем логику для объявления переменной login и задания её случайного значения:
    public static String getRandomLogin() {
      String login = faker.name().username();
        return login;
    }
    //добавляем логику для объявления переменной password и задания её случайного значения:
    public static String getRandomPassword() {
      String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        //создаем пользователя user используя методы getRandomLogin(), getRandomPassword() и параметр status:
        public static RegistrationDto getUser(String status) { //создает пользователя по шаблону дата класса
            var user = new RegistrationDto(getRandomLogin(),getRandomPassword(), status);
            return user;
        }
        //объявляем переменную registeredUser и присваиваем ей значение возвращённое getUser(status), посылаем запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser):
        public static RegistrationDto getRegisteredUser(String status) {
          var registeredUser = getUser(status); //вызывает метод генерации пользователя
          sendRequest(registeredUser); //отправляет запрос с помощью ранее реализованного метода sendRequest
            return registeredUser; //возвратит в тесты зарегистрированного пользователя как активного так и неактивного, со случайным логином и паролем
        }
    }
//структура json запроса в кот. мы передаем пользователя на бэк через хттп должна быть описана в дата классе, имена полей должны соответствовать именам полей кот уходят в json запросе:
    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
