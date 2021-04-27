import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static Utils.Edpoints.IMAGE_DEL_HASH;
import static io.restassured.RestAssured.given;

public class ImageDeleteTests extends BaseTests {
    static String token;
    static String ClientID;
    static String imageName;
    static String imageDeleteHash;
    static ResponseSpecification responseSpecification;
    static RequestSpecification requestSpecID;
    static RequestSpecification requestSpecToken;


    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        token = properties.getProperty("token");
        RestAssured.baseURI = properties.getProperty("base.url");

        ClientID = properties.getProperty("ClientID");
        imageDeleteHash = properties.getProperty("imageDeleteHash");
    }

    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()

                .expectStatusCode(200)
                .expectBody("data", CoreMatchers.equalTo(true))
                .build();

        requestSpecID = new RequestSpecBuilder()
                .addHeader("Authorization", ClientID)
                .setAccept(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();

        requestSpecToken = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();

    }


    @Epic("Тестирование https://imgur.com на удаление изображения")
    @Feature(value = "DeleteImage")
    @Description("Удаление имеющегося изображения")

    @Test
    void DeleteImageUnAuthedTest() {
        given()
                .spec(requestSpecID)
                .when()
                .delete(IMAGE_DEL_HASH,imageDeleteHash)
                .then()
                .spec(responseSpecification);
    }

    @Epic("Тестирование https://imgur.com на удаление изображения")
    @Feature(value = "DeleteImage")
    @Description("Удаление не существующего изображения")

    @Test
    void DeleteImageAuthedTest() {
        given()
                .spec(requestSpecToken)
                .when()
                .delete(IMAGE_DEL_HASH, "DIjJRjt")
                .then()
                .spec(responseSpecification);
    }
}
