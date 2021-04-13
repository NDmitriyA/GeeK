import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UpdateImageInforTests extends BaseTests{
    static String token;
    static String ClientID;
    static String imageHash;
    static String imageDeleteHash;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        token = properties.getProperty("token");
        RestAssured.baseURI = properties.getProperty("base.url");
        ClientID = properties.getProperty("ClientID");
        imageHash = properties.getProperty("imageHash");
        imageDeleteHash = properties.getProperty("imageDeleteHash");
    }


    @Test
    void UpdateTitleInfoUnAuthedTest() {
        given()
                .param("title","qwertyuiop")
                .header("Authorization", ClientID)
                .log()
                .all()
                .when()
                .post("image/{imageDeleteHash}",imageDeleteHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));


    }
    @Test
    void UpdateTitleInfoUn1AuthedTest() {
        given()
                .param("description","This is an image of a heart outline.")
                .header("Authorization", ClientID)
                .log()
                .all()
                .when()
                .post("image/{imageDeleteHash}",imageDeleteHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));


    }
    @Test
    void UpdateTitleInfoUn2AuthedTest() {
        given()
                .param("description", "This is an image.")
                .param("title","poiuytrewq")
                .header("Authorization", ClientID)
                .log()
                .all()
                .when()
                .post("image/{imageDeleteHash}", imageDeleteHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }
    @Test
    void UpdateTitleInfoAuthedTest() {
        given()
                .param("description", "This is Simpson.")
                .param("title","2021")
                .header("Authorization", token)
                .log()
                .all()
                .when()
                .post("image/{imageName}", imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }
    @Test
    void UpdateTitleInfo1AuthedTest() {
        given()
                .param("description", " BIG Simpson.")
                .header("Authorization", token)
                .log()
                .all()
                .when()
                .post("image/{imageName}", imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }
    @Test
    void UpdateTitleInfo2AuthedTest() {
        given()
                .param("title","HABR")
                .header("Authorization", token)
                .log()
                .all()
                .when()
                .post("image/{imageName}", imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }
    @Test
    void FavoritImageTest() {
        given()
                .header("Authorization", token)
                .log()
                .all()
                .when()
                .post("image/{imageName}/favorite", imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo("favorited"));
    }
}
