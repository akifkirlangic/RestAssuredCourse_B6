import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class NationalitiesFunctionTests6 {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private String nationalityId;

    HashMap<String, String> reqBody = new HashMap<>();



    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io/";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

    }

    @Test
    public void loginTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();

    }

    @Test(dependsOnMethods = "loginTest")
    public void createNationalityTest() {

        reqBody.put("name", "AkifNationallitty991");

        nationalityId = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "createNationalityTest")
    public void createNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createNationalityNegativeTest")
    public void getNationalityAndValidate(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/"+nationalityId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(reqBody.get("name")));

    }

    @Test(dependsOnMethods = "getNationalityAndValidate")
    public void editNationalityTest(){

        HashMap<String,String> updateReqBody = new HashMap<>();
        updateReqBody.put("name","UpddateAkifNationality991");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updateReqBody.get("name")));

    }

    @Test(dependsOnMethods = "editNationalityTest")
    public void deleteNationalityTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/"+nationalityId)
                .then()
                .log().body()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "deleteNationalityTest")
    public void deleteNationalityNegativeTest(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/"+nationalityId)
                .then()
                .log().body()
                .statusCode(404);

    }


}
