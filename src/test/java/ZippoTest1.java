import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class ZippoTest1 {

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://api.zippopotam.us";

    }


    @Test
    public void test() {

        given()
                .when()
                .then();

    }

    @Test
    public void checkingStatusCode() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .statusCode(200);
    }

    @Test
    public void loggingRequestDetails() {

        given()
                .log().all()
                .when()
                .get("/tr/05100")
                .then()
                .statusCode(200);
    }

    @Test
    public void loggingResponseDetails() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void checkContentType() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);
    }

    @Test
    public void validateCountryTest() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .body("country", equalTo("Turkey")) // country field from the response body eşit olmalı country value from the response body  -> field = value olmalı amasya = turkey gibi
                .statusCode(200);

    }

    @Test
    public void validateCountryAbv() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .body("'country abbreviation'", equalTo("TR")) // if the field name has a space and then you are gonna need to cover it wit single quote (')
                .statusCode(200);
    }

    @Test
    public void validateState() {

        given()
                .when()
                .get("/tr/05100")
                .then()
                .body("places[17].'place name'", equalTo("Hacilar Meydani Mah."))
                .statusCode(200);

    }
    // places[1].country  ->  {country2,state2,zipcode2}  bir array ornegi yazdım aşagıda places adında ve onun objelerini bu şekilde seçiyoruz
    // places array -> [ {country1,state1,zipcode1}, {country2,state2,zipcode2}, {country3,state3,zipcode3} ]


    @Test
    public void pathParametersTest() {

        String country = "tr";
        String zipcode = "05100";

        given()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode)
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .statusCode(200);

    }

    @Test
    public void queryParameters() {

        String gender = "female";
        String status = "active";

        given()
                .param("gender",gender)
                .param("status",status)
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body();

    }

    @Test public void extractValueTest() {

        Object countryInfo = given() // object variabledır burası (request body)
                .when()
                .get("/tr/05100")
                .then()
                .extract().path("country"); // request sonucu oluşan response tan variable alıyoruz

        System.out.println(countryInfo);
    }

}
