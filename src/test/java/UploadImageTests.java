import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class UploadImageTests extends BaseTests {
    static final String INPUT_IMG_FILE_PATH = "src/test/resources/Simpson.jpg";
    static final String INPUT_64_FILE_PATH = "src/test/resources/simtoo.jpg";
    static final String INPUT_BIGIMG_FILE_PATH ="src/test/resources/visokogo.jpg";
    static byte[] fileContent;
    static Properties properties;
    static String token;
    static String username;
    static String ClientID;
    static String imageHash;
    static String uploadedImageId;


    @BeforeAll
    static void beforeAll() throws IOException {
        properties = new Properties();
        File input64File = new File(INPUT_64_FILE_PATH);
        try {
            fileContent = readFileToByteArray(input64File);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        token = properties.getProperty("token");
        username = properties.getProperty("username");
        RestAssured.baseURI = properties.getProperty("base.url");
        ClientID = properties.getProperty("ClientID");
        imageHash = properties.getProperty("imageHash");

    }

    @Test
    void uploadImageTest() {
        given()
                .multiPart("image", new File(INPUT_IMG_FILE_PATH))
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void uploadBigImageTest() {
        given()
                .multiPart("image", new File(INPUT_BIGIMG_FILE_PATH))
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(400)
                .body("data.error", CoreMatchers.equalTo("File is over the size limit"));
    }


    @Test
    void getImageTest() {
        given()
                .header("Authorization", ClientID)
                .log()
                .all()
                .when()
                .get("image/{imageName}", imageHash)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .contentType(ContentType.JSON)
                .body("data.id", CoreMatchers.equalTo(imageHash))
                .body("data.link", CoreMatchers.equalTo("https://i.imgur.com/KYKvolw.jpg"));
    }

    @Test
    void uploadImageFromBase64Test() {
        String fileContentBase64 = Base64.encodeBase64String(fileContent);
        uploadedImageId = given()
                .multiPart("image", fileContentBase64)
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void uploadImagineTest() {
        given()
                .multiPart("image", new File(INPUT_IMG_FILE_PATH))
                .param("name", "Simpson")
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.name", CoreMatchers.equalTo("Simpson"));
    }

    @Test
    void uploadImgtitleTest() {
        given()
                .multiPart("image", new File(INPUT_IMG_FILE_PATH))
                .param("title", "900x900")
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.title", CoreMatchers.equalTo("900x900"));
    }

    @Test
    void uploadImageDescryTest() {
        given()
                .multiPart("image", new File(INPUT_IMG_FILE_PATH))
                .param("description", "This is an 900x900 pixel image.")
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.description", CoreMatchers.equalTo("This is an 900x900 pixel image."));
    }

    @Test
    void uploadAllTest() {
        given()
                .multiPart("image", new File(INPUT_IMG_FILE_PATH))
                .param("name", "Simpson")
                .param("title", "800x800")
                .param("description", "This is an 900x900 pixel image.")
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.title", CoreMatchers.equalTo("800x800"))
                .body("data.name", CoreMatchers.equalTo("Simpson"))
                .body("data.description", CoreMatchers.equalTo("This is an 900x900 pixel image."));
    }

}
