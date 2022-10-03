import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountriesFunctionTests5 {

    private RequestSpecification reqSpec;

    private Cookies cookies; // we declare cookies variable here

    private String countryId;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io/";


        reqSpec = given()
                .log().body()
                // burda headers ve token kullanamızın sebebi otomatik olarak cookiesden(postmandeki) eld ediliyor.
                .contentType(ContentType.JSON);
    }

    @Test
    public void loginTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        // we are initializing it inside this login test (bu giriş testinin içinde başlatıyoruz)
        cookies = given() // en aşağıdaki cookies response bodyden  alınanlar informationlar(token dahil) cookies variable içine store lanıyor.cünkü bu informationmları kullancaz sonra
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200) // buraya kadar request
                .extract().detailedCookies(); // burdanda request e karşılık gelen response bodyden  cookise detaylarını alıyoruz(token da burdan geliyo ve cookies değişkenine depolıyoruz)

    }

    @Test(dependsOnMethods = "loginTest")
    public void createCountryTest() {

        HashMap<String, String> reqBody = new HashMap<>(); // bilgileri burada hashmap imize depoladık
        reqBody.put("name", "AkifCountry9111");
        reqBody.put("code", "AC9");
        reqBody.put("hasState", "false");

        countryId = given() // countryId ye depoladık response body den aldıgımız id yi
                .spec(reqSpec)
                .cookies(cookies) // autorization için header kullanıyoduk gorestte. campuste ise cookise kullanıyoruz
                .body(reqBody)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .body("code", equalTo(reqBody.get("code")))
                .extract().jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "createCountryTest")
    public void editCountryTest() {

        HashMap<String, String> updateReqBody = new HashMap<>();
        updateReqBody.put("id", countryId);
        updateReqBody.put("name", "UpdateAkifCountry991");
        updateReqBody.put("code", "UAC9");
        updateReqBody.put("hasState", "false");


        given()
                .spec(reqSpec) // specification content type
                .cookies(cookies) // autorizatiton
                .body(updateReqBody)
                .when()
                .put("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateReqBody.get("name")));

    }

    @Test(dependsOnMethods = "editCountryTest")
    public void deleteCountryTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + countryId)
                .then()
                .log().body()
                .statusCode(200);

    }

}
