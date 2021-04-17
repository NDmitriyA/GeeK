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

import static Utils.Edpoints.*;
import static io.restassured.RestAssured.given;

public class UpdateImageInforTests extends BaseTests {
    static String token;
    static String ClientID;
    static String imageHash;
    static String imageDeleteHash;
    static ResponseSpecification responseSpecification;
    static RequestSpecification requestSpecID;
    static RequestSpecification requestSpecToken;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));

        RestAssured.baseURI = properties.getProperty("base.url");
        token = properties.getProperty("token");
        ClientID = properties.getProperty("ClientID");
        imageHash = properties.getProperty("imageHash");
        imageDeleteHash = properties.getProperty("imageDeleteHash");

    }
    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()

                .expectStatusCode(200)
                .log(LogDetail.ALL)
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




    @Test
    void UpdateTitleInfoUnAuthedTest() {
        given()
                .param("title", "qwertyuiop")
                .spec(requestSpecID)
                .when()
                .post(IMAGE_DEL_HASH, imageDeleteHash)
                .then()
                .spec(responseSpecification);


    }

    @Test
    void UpdateTitleInfoUn1AuthedTest() {
        given()
                .param("description", "This is an image of a heart outline.")
                .spec(requestSpecID)
                .when()
                .post(IMAGE_DEL_HASH, imageDeleteHash)
                .then()
                .spec(responseSpecification);


    }

    @Test
    void UpdateTitleInfoUn2AuthedTest() {
        given()
                .param("description", "This is an image.")
                .param("title", "poiuytrewq")
                .spec(requestSpecID)
                .when()
                .post(IMAGE_DEL_HASH, imageDeleteHash)
                .then()
                .spec(responseSpecification);
    }

    @Test
    void UpdateTitleInfoAuthedTest() {
        given()
                .param("description", "This is Simpson.")
                .param("title", "2021")
                .spec(requestSpecToken)
                .when()
                .post(GET_IMAGE_HASH, imageHash)
                .then()
                .spec(responseSpecification);
    }

    @Test
    void UpdateTitleInfo1AuthedTest() {
        given()
                .param("description", " BIG Simpson.")
                .spec(requestSpecToken)
                .when()
                .post(GET_IMAGE_HASH, imageHash)
                .then()
                .spec(responseSpecification);
    }

    @Test
    void UpdateTitleInfo2AuthedTest() {
        given()
                .param("title", "HABR")
                .spec(requestSpecToken)
                .when()
                .post(GET_IMAGE_HASH, imageHash)
                .then()
                .spec(responseSpecification);
    }

    @Test
    void FavoritImageTest() {
        given()
                .spec(requestSpecToken)
                .post(GET_IMAGE_HASH_FAV, imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo("favorited"));
    }
}
