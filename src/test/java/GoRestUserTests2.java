import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTests2 {

    private RequestSpecification reqSpec;
    private HashMap<String, String> requestBody;
    private Object userId;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in/";

        reqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer c1255aa12f785311c4368192b304f4ef8668dd6f10a13510dfa20b6fab5d3873")
                .contentType(ContentType.JSON);
        // reqSpec  i yaptık cünkü edit delete create  te filan da  tekrar kullanmak için

        // java da json formatı yoktur postmadeki gibi bu yüzden hashmap kullanıyoruz formatı benzer oldugu için (key,value) value değişebilir:)
        // because we need to (key,value) pairs here
        requestBody = new HashMap<>();  // burası bize mapleri hatırlatıyor cünkü key value şeklinde çalışıyo yani yani hash mapin içine storeluyoruz
                                        //  map object tir burası = request body deki json objecttir.
                                        // pomxl e bak hashmapi json a conveert etmek için dependency yazdık
        requestBody.put("name", "TechnoTest User1991"); // -> (key,value) pairs
        requestBody.put("email", "tesasatus19911@techno.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
    }


    @Test
    public void createUserTest() {

        userId = given() // burada datayı userId içine depoluyoruz
                .spec(reqSpec)
                .body(requestBody) // buraya kadar olan kısımda test data mızı set ettik
                .when() // buradan itibaren ise request methodu(post) ve request url("/public/v2/users") 'i kullandık
                .post("/public/v2/users")
                .then() //  validation part (test kısmı)
                .log().body() // response body details
                .body("name",equalTo(requestBody.get("name"))) // hamcrest uyguladık
                .statusCode(201)
                .extract().path("id"); // we will be also extracting id field -> userId nin içindeki id yi alıyoruz burdan

    }

    @Test(dependsOnMethods = "createUserTest")
    public void editUserTest() {

        HashMap<String, String> updateRequestBody = new HashMap<>();
        updateRequestBody.put("name", "UpdatedMAK");

        given()
                .spec(reqSpec)
                .body(updateRequestBody)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest() {

        //HashMap<String,String> r

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/"+userId)
                .then()
                .log().body()
                .statusCode(204);

    }

}
