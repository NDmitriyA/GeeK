import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ImageDeleteTests extends BaseTests {
    static String token;
    static String ClientID;
    static String imageName;
    static String imageDeleteHash;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        token = properties.getProperty("token");
        RestAssured.baseURI = properties.getProperty("base.url");
        ClientID = properties.getProperty("ClientID");
        imageName = properties.getProperty("imageName");
        imageDeleteHash = properties.getProperty("imageDeleteHash");
    }

    @Test
    void DeleteImageUnAuthedTest() {
        given()
                .header("Authorization", ClientID)
                .log()
                .all()
                .when()
                .delete("image/{imageDeleteHash}",imageDeleteHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }

    @Test
    void DeleteImageAuthedTest() {
        given()
                .header("Authorization", token)
                .log()
                .all()
                .when()
                .delete("image/{imageDeleteHash}","DIjJRjt")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data", CoreMatchers.equalTo(true));
    }
}
