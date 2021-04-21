import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static Utils.Edpoints.POST_IMAGE;
import static io.restassured.RestAssured.given;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class UploadImageTests extends BaseTests {
    static final String INPUT_IMG_FILE_PATH = "src/test/resources/Simpson.jpg";
    static final String INPUT_64_FILE_PATH = "src/test/resources/simtoo.jpg";
    static final String INPUT_BIGIMG_FILE_PATH = "src/test/resources/visokogo.jpg";
    static byte[] fileContent;
    static Properties properties;
    static String token;
    static String username;
    static String ClientID;
    static String imageHash;
    static ValidatableResponse uploadedImageId;
    static ResponseSpecification responseSpecification;
    static RequestSpecification requestSpecID;
    static RequestSpecification requestSpecToken;
    static MultiPartSpecification multiPartspec;
    static MultiPartSpecification multiBigPartspec;



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

    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()

                .expectStatusCode(200)
                .expectBody("success", CoreMatchers.equalTo(true))
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

        multiPartspec = new MultiPartSpecBuilder(new File(INPUT_IMG_FILE_PATH))
                .controlName("image")
                .build();

        multiBigPartspec = new MultiPartSpecBuilder(new File(INPUT_BIGIMG_FILE_PATH))
                .controlName("image")
                .build();

    }

    @Test
    void uploadImageTest() {
        given()
                .multiPart(multiPartspec)
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void uploadBigImageTest() {
        given()
                .multiPart(multiBigPartspec)
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .then()
                .log()
                .ifStatusCodeIsEqualTo(400)
                .body("data.error", CoreMatchers.equalTo("File is over the size limit"));
    }



    @Test
    void uploadImageFromBase64Test() {
        String fileContentBase64 = Base64.encodeBase64String(fileContent);
        uploadedImageId = given()
                .multiPart("image", fileContentBase64)
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
               .spec(responseSpecification);
    }

    @Test
    void uploadImagineTest() {
        given()
                .multiPart(multiPartspec)
                .param("name", "Simpson")
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.name", CoreMatchers.equalTo("Simpson"));
    }

    @Test
    void uploadImgtitleTest() {
        given()
                .multiPart(multiPartspec)
                .param("title", "900x900")
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.title", CoreMatchers.equalTo("900x900"));
    }

    @Test
    void uploadImageDescryTest() {
        given()
                .multiPart(multiPartspec)
                .param("description", "This is an 900x900 pixel image.")
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.description", CoreMatchers.equalTo("This is an 900x900 pixel image."));
    }

    @Test
    void uploadAllTest() {
        given()
                .multiPart(multiPartspec)
                .param("", "Simpson")
                .param("title", "800x800")
                .param("description", "")
                .spec(requestSpecToken)
                .when()
                .post(POST_IMAGE)
                .prettyPeek()
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200)
                .body("data.title", CoreMatchers.equalTo("800x800"))
                .body("data.name", CoreMatchers.equalTo("Simpson"))
                .body("data.description", CoreMatchers.equalTo("This is an 900x900 pixel image."));
    }

}
