package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CommonSteps extends BaseSteps{

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
