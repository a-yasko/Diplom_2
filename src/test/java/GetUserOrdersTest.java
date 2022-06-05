import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static steps.Steps.*;

public class GetUserOrdersTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Get orders without authorization")
  public void getOrdersWithoutAuth() {
    Response response = doGetRequest("/api/orders");
    checkStatusCode(response, 401);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Get orders with authorization")
  public void getOrdersWithAuth() {
    String accessToken = createUser();

    List<String> ingredients = List.of(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa72"
    );

    createOrder(ingredients, accessToken);

    Response response = doGetRequest("/api/orders", accessToken);
    checkStatusCode(response, 200);
    checkResponseBody(response, "success", true);

    deleteUser(accessToken);
  }
}
