

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public abstract class BaseTests {
    protected static Map<String, String> headers = new HashMap<>();
    protected static String username;
    static Properties properties = new Properties();
    static ResponseSpecification responseSpecification;
    static RequestSpecification requestSpecID;
    static RequestSpecification requestSpecToken;
    static String token;
    static String ClientID;


    @BeforeAll
    static void beforeAll() throws IOException {
        loadProperties();
        RestAssured.baseURI = properties.getProperty("base.url");
        RestAssured.requestSpecification = requestSpecID;
        RestAssured.requestSpecification = requestSpecToken;
        headers.put("Authorization", properties.getProperty("token"));
        token = properties.getProperty("token");
        ClientID = properties.getProperty("ClientID");
        username = properties.getProperty("username");
        System.out.println(username);

    }


    private static void loadProperties() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));

    }


}


