package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Steps {
  private static String JSON = "application/json";

  @Step("Do post request '{uri}'")
  public static Response doPostRequest(Object body, String uri) {
    return given()
            .header("Content-type", JSON)
            .and()
            .body(body)
            .when()
            .post(uri);
  }

  @Step("Do patch request '{uri}'")
  public static Response doPatchRequest(Object body, String uri, String accessToken) {
    return given()
            .header("Content-type", JSON)
            .header("Authorization", accessToken)
            .and()
            .body(body)
            .when()
            .patch(uri);
  }

  @Step("Check status code")
  public static void checkStatusCode(Response response, Integer statusCode) {
    response.then().assertThat().statusCode(statusCode);
  }

  @Step("Check response body")
  public static void checkResponseBody(Response response, String key, Object value) {
    response.then().assertThat().body(key, equalTo(value));
  }

  @Step("Delete user")
  public static void deleteUser(String accessToken) {
    Response response = given()
            .header("Authorization", accessToken)
            .delete("/api/auth/user");
    checkStatusCode(response, 202);
    checkResponseBody(response, "success", true);
  }
}
