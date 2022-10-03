import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserFunction3 {

    private RequestSpecification reqSpec;  // tüm metodlarda kullanmak için burda tanıttım(declare). bu classa özel kılmak için private dedi. aynısı postman da da var

    private HashMap<String, String> requestBody;
    private Object userId;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in/"; // we setup urı

        reqSpec = given() // tekrar kullanılabilir reqSpec
                .log().body() // request body
                .header("Authorization", "Bearer c1255aa12f785311c4368192b304f4ef8668dd6f10a13510dfa20b6fab5d3873")
                .contentType(ContentType.JSON);
        requestBody = new HashMap<>();  // map object tir burası = request body deki json objecttir

        requestBody.put("name", "testUserName911");
        requestBody.put("email", "testUss@techmnho.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");


    }

    @Test
    public void createUserTest() {

        // yeni olusturdugumuz kullanıcıyı buraya(userId) depoladık
        userId = given() // ek olarak userId Objesinin sonucları(id,name,gender) bu sürecin sonunda çıkıyor
                //we setup reqst body, header, content type
                //.log().body()
                //.header("Authorization","Bearer c1255aa12f785311c4368192b304f4ef8668dd6f10a13510dfa20b6fab5d3873")
                //.contentType(ContentType.JSON)
                // bunların yerine -> spec(reqSpec) bunu kullandık
                .spec(reqSpec)
                .body(requestBody)
                .when() // burada request methodu(post) ve request url("/public/v2/users") 'i kullandık
                .post("/public/v2/users")
                .then() // validation part (test kısmı)
                .log().body() // response body details
                .statusCode(201)
                .body("name", equalTo(requestBody.get("name"))) // hamcrest uyguladık response body(""name )ve request bodydeki(requestBody.get("name")) veriyi karşılaştırdık
                .extract().path("id");

    }

    @Test(dependsOnMethods = "createUserTest")  // birinci test çalışmalı sonrada bu test ona bağlı calısmalı
    public void createUserNegativeTest() {


        given()
                .spec(reqSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422) // burdan sonra yeni kullanıcı olusturamadığını bildiğimiz için userid olusturamadığınıda bikliyoruz.
                                   // o yuzden aşağıdakişeyleri yapmamzıın bi anlamı yok karlşılığı yok çünkü id ıolusmuyo
                .body("message[0]", equalTo("has already been taken"));

        //.body("name",equalTo(reqBodyNeg.get("name")))
        //.extract().path("id");
    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void getUserAndValidate() {

        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users/"+userId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(requestBody.get("name")))
                .body("email",equalTo(requestBody.get("email")))
                .body("gender",equalTo(requestBody.get("gender")))
                .body("status",equalTo(requestBody.get("status")));

    }

    @Test(dependsOnMethods = "getUserAndValidate")
    public void editUserTest() {

        HashMap<String, String> reqBodyUpdate = new HashMap<>();
        reqBodyUpdate.put("name", "updateTestUser Name91");

        given()
                .spec(reqSpec)
                .body(reqBodyUpdate)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo(reqBodyUpdate.get("name")));  // resBody ("name") = reqBodyUpdate.get("name") karşılaştırıp dogrulama yapıyoruz

    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));

    }


}
