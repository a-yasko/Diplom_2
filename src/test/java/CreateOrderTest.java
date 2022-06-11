import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static data.TestData.*;
import static steps.BaseSteps.*;
import static steps.CommonSteps.*;

public class CreateOrderTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBaseUrl();
  }

  @Test
  @DisplayName("Create order without authorization")
  public void createOrderWithoutAuth() {
    createOrder(getIngredientKit1());
  }

  @Test
  @DisplayName("Create order without authorization and without ingredients")
  public void createOrderWithoutAuthAndWithoutIngredients() {
    Order order = new Order(List.of());
    Response response = doPostRequest(order, "/api/orders");
    checkStatusCode(response, 400);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Create order without authorization and with invalid hash")
  public void createOrderWithoutAuthAndWithInvalidHash() {
    Order order = new Order(getIngredientWithInvalidHash());
    Response response = doPostRequest(order, "/api/orders");
    checkStatusCode(response, 500);
  }

  @Test
  @DisplayName("Create order with authorization")
  public void createOrderWithAuth() {
    String accessToken = createUser();

    createOrder(getIngredientKit2(), accessToken);

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Create order with authorization and without ingredients")
  public void createOrderWithAuthAndWithoutIngredients() {
    String accessToken = createUser();

    Order order = new Order(List.of());
    Response createOrder = doPostRequest(order, "/api/orders", accessToken);
    checkStatusCode(createOrder, 400);
    checkResponseBody(createOrder, "success", false);

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Create order with authorization and with invalid hash")
  public void createOrderWithAuthAndWithInvalidHash() {
    String accessToken = createUser();

    Order order = new Order(getIngredientWithInvalidHash());
    Response createOrder = doPostRequest(order, "/api/orders", accessToken);
    checkStatusCode(createOrder, 500);

    deleteUser(accessToken);
  }
}