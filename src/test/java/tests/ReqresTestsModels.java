package tests;
import io.restassured.response.ValidatableResponse;
import models.ErrorModel;
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
import static specs.LoginSpec.loginRequestSpec;

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
        UserModel response = step("Create user", () ->
                given(loginRequestSpec)
                        .body(createBody)
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(201).extract().as(UserModel.class));

        step("Check response name", () ->
                assertThat(response.getName()).isEqualTo("eve.holt@reqres.in"));
        step("Check response job", () ->
                assertThat(response.getJob()).isEqualTo("cityslicka"));

    }


    @Test
    @DisplayName("Successful login")
    void successfulLoginTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Successful login", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Unsuccessful login")
    void unSuccessfulLoginWithMissingEmailTest() {
        LoginBodyLombokModel unsuccessfulBody = new LoginBodyLombokModel();
        unsuccessfulBody.setPassword("cityslicka");

        ErrorModel errorModel = step("Login with missing email", () ->
                        given(loginRequestSpec)
                        .body(unsuccessfulBody)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(400)
                                .extract().as(ErrorModel.class));

        step("Check error text", () ->
                assertThat(errorModel.getError()).isEqualTo("Missing email or username"));
    }

    @Test
    @DisplayName("Unsuccessful login with missing password")
    void unSuccessfulLoginWithMissingPasswordTest() {
        LoginBodyLombokModel unsuccessfulLoginBody = new LoginBodyLombokModel();
        unsuccessfulLoginBody.setEmail("eve.holt@reqres.in");
        ErrorModel errorModel = step("Login with missing password", () ->
                given(loginRequestSpec)
                .body(unsuccessfulLoginBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(ErrorModel.class));

        step("Check error text", () ->
                assertThat(errorModel.getError()).isEqualTo("Missing password"));
    }


    @Test
    @DisplayName("Unsuccessful login with empty data")
    void unSuccessfulLoginWithEmptyDataTest() {
        ValidatableResponse response = step("Send empty body reguest", () ->
                given()
                        .log().uri()
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status());

        step("Check status code 415", () ->
                response.statusCode(415)
        );
    }
}
