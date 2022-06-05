package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Steps {
  private static String JSON = "application/json";

  @Step("Do get request '{uri}'")
  public static Response doGetRequest(String uri) {
    return given().get(uri);
  }

  @Step("Do get request '{uri}'")
  public static Response doGetRequest(String uri, String accessToken) {
    return given()
            .header("Authorization", accessToken)
            .when()
            .get(uri);
  }

  @Step("Do post request '{uri}'")
  public static Response doPostRequest(Object body, String uri) {
    return given()
            .header("Content-type", JSON)
            .and()
            .body(body)
            .when()
            .post(uri);
  }

  @Step("Do post request '{uri}'")
  public static Response doPostRequest(Object body, String uri, String accessToken) {
    return given()
            .header("Content-type", JSON)
            .header("Authorization", accessToken)
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

  @Step("Create user")
  public static String createUser() {
    User user = new User(
            RandomStringUtils.randomAlphabetic(10) + "@test.ru",
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(10)
    );
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 200);
    checkResponseBody(response, "success", true);
    return response.path("accessToken");
  }

  @Step("Create order")
  public static void createOrder(List<String> ingredients) {
    Order order = new Order(ingredients);
    Response response = doPostRequest(order, "/api/orders");
    checkStatusCode(response, 200);
    checkResponseBody(response, "success", true);
  }

  @Step("Create order")
  public static void createOrder(List<String> ingredients, String accessToken) {
    Order order = new Order(ingredients);
    Response response = doPostRequest(order, "/api/orders", accessToken);
    checkStatusCode(response, 200);
    checkResponseBody(response, "success", true);
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
