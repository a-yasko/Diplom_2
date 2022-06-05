import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static steps.Steps.*;
import static steps.Steps.createOrder;

public class CreateOrderTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Create order without authorization")
  public void createOrderWithoutAuth() {
    List<String> ingredients = List.of(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f"
    );

    createOrder(ingredients);
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
    Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d1", "61c0c5a71d1f82001bdaaa6f1"));
    Response response = doPostRequest(order, "/api/orders");
    checkStatusCode(response, 500);
  }

  @Test
  @DisplayName("Create order with authorization")
  public void createOrderWithAuth() {
    String accessToken = createUser();

    List<String> ingredients = List.of(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa72"
    );

    createOrder(ingredients, accessToken);

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

    Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d1", "61c0c5a71d1f82001bdaaa6f1"));
    Response createOrder = doPostRequest(order, "/api/orders", accessToken);
    checkStatusCode(createOrder, 500);

    deleteUser(accessToken);
  }
}