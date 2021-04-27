import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
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
import java.util.Properties;

import static Utils.Edpoints.GET_IMAGE_HASH;
import static io.restassured.RestAssured.given;

public class getImageTest extends BaseTests {
    static RequestSpecification requestSpecID;
    static String imageHash;
    static ResponseSpecification responseSpecGetImage;
    static ResponseSpecification responseSpecGetNoImage;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        ClientID = properties.getProperty("ClientID");
        imageHash = properties.getProperty("imageHash");
        RestAssured.baseURI = properties.getProperty("base.url");
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeEach
    void beforeTest() {
        requestSpecID = new RequestSpecBuilder()
                .addHeader("Authorization", ClientID)
                .setAccept(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();



        responseSpecGetImage = new ResponseSpecBuilder()

                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .expectBody("data.id", CoreMatchers.equalTo(imageHash))
                .expectBody("data.link", CoreMatchers.equalTo("https://i.imgur.com/1H1iKeT.jpg"))
                .build();

        responseSpecGetNoImage =  new ResponseSpecBuilder()

                .expectStatusCode(404)
                .expectStatusLine("HTTP/1.1 404 Not Found")
                .log(LogDetail.ALL)
                .build();
    }

    @Epic("Тестирование https://imgur.com на просмотр изображения")
    @Feature(value = "getImage")
    @Description("Просмотр имеющегося изображения")

    @Test
    void getImageTest() {
        given()
                .spec(requestSpecID)
                .when()
                .get(GET_IMAGE_HASH, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecGetImage);
    }

    @Epic("Тестирование https://imgur.com на просмотр изображения")

    @Test
    void getNoImageTest() {
        given()
                .spec(requestSpecID)
                .when()
                .get(GET_IMAGE_HASH, "drUwFuL")
                .then()
                .spec(responseSpecGetNoImage);
    }

}