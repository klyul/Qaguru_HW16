package tests;
import models.LoginBodyLombokModel;
import models.LoginResponseLombokModel;
import models.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Reqres tests models")
@Tag("API")

public class ReqresTestsModels {

        /*
        1. Make request (POST) to https://reqres.in/api/login with body { "email": "eve.holt@reqres.in", "password": "cityslicka" }
        2. Get response { "token": "QpwL5tke4Pnpja7X4" }
        3. Check token is QpwL5tke4Pnpja7X4
     */

    @Test
    @DisplayName("Create user")
    void createUserTest() {
        UserModel createBody = new UserModel();
        createBody.setName("eve.holt@reqres.in");
        createBody.setJob("cityslicka");
        UserModel response = given()
                .log().uri()
                .body(createBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201).extract().as(UserModel.class);

    }


    @Test
    @DisplayName("Successful login")
    void successfulLoginTest() {
    LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

    LoginResponseLombokModel response = given()
            .log().uri()
            .body(loginBody)
            .contentType(JSON)
            .when()
            .post("https://reqres.in/api/login")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .extract().as(LoginResponseLombokModel.class);

    assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
}

    @Test
    @DisplayName("Unsuccessful login")
    void unSuccessfulLoginWithMissingEmailTest() {
        LoginBodyLombokModel UnsuccessfulBody = new LoginBodyLombokModel();
        UnsuccessfulBody.setEmail("eve.holt@reqres.in");
        UnsuccessfulBody.setPassword("cityslicka");
        LoginResponseLombokModel response = given()
                .log().uri()
                .body(UnsuccessfulBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("Missing email or username");
    }

    @Test
    @DisplayName("Unsuccessful login with missing password")
    void unSuccessfulLoginWithMissingPasswordTest() {
        LoginBodyLombokModel UnsuccessfulLoginBody = new LoginBodyLombokModel();
        UnsuccessfulLoginBody.setEmail("eve.holt@reqres.in");
        LoginResponseLombokModel response = given()
                .log().uri()
                .body(UnsuccessfulLoginBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is(null))
                .extract().as(LoginResponseLombokModel.class);
    }


    @Test
    @DisplayName("Unsuccessful login with empty data")
    void unSuccessfulLoginWithEmptyDataTest() {
        LoginBodyLombokModel UnsuccessfulEmptyBody = new LoginBodyLombokModel();
        UnsuccessfulEmptyBody.setEmail(" ");
        UnsuccessfulEmptyBody.setPassword(" ");
        LoginResponseLombokModel response = given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .statusCode(415)
                .extract().as(LoginResponseLombokModel.class);;
    }

}