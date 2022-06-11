import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.getIngredientKit2;
import static steps.BaseSteps.*;
import static steps.CommonSteps.*;

public class GetUserOrdersTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBaseUrl();
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

    createOrder(getIngredientKit2(), accessToken);

    Response response = doGetRequest("/api/orders", accessToken);
    checkStatusCode(response, 200);
    checkResponseBody(response, "success", true);

    deleteUser(accessToken);
  }
}