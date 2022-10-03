import Pojo.GoRestUser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTestWithPojo {

    private RequestSpecification reqSpec;

    private GoRestUser user;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in/";

        reqSpec = given()
                .log().body()
                .header("Authorization", "Bearer c1255aa12f785311c4368192b304f4ef8668dd6f10a13510dfa20b6fab5d3873")
                .contentType(ContentType.JSON);

        // GoRestUser class ının yeni objesi
        user = new GoRestUser(); // hashmap yerine kullandık . burda pojoda hashmape gerek yok
        user.setName("Luis Figo");
        user.setEmail("luisFigo@tchno.com");
        user.setGender("male");
        user.setStatus("active");

    }


    @Test
    public void createUserTest() {

        // burada userid objesi olusturmuyoruz cünkü clasımız var ordan string olarak alabiliyoruz

        // pojodan once olusturdugumuz user id yi bu şekilde olusturuyoruz -> user.setId(tüm request)

        user.setId(given()
                .spec(reqSpec)
                .body(user)   // hashmap yerine java object kullandık request body de
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(user.getName()))   // response body("name") = request body (user.getName())
                .extract().jsonPath().getString("id"));

    }

    @Test(dependsOnMethods = "createUserTest")
    public void createUserNegativeTest() {

        given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422);

    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void getUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(user.getName()))
                .body("email", equalTo(user.getEmail()))
                .body("gender", equalTo(user.getGender()))
                .body("status", equalTo(user.getStatus()));

    }

    @Test(dependsOnMethods = "getUserTest")
    public void editUserTest() {

        HashMap<String, String> body = new HashMap<>();
        body.put("status", "inactive");

        given()
                .spec(reqSpec)
                .body(body)
                .when()
                .put("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("status", equalTo(body.get("status")));

    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest() {

        given()
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(404);
    }

}
