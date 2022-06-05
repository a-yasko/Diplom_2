import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static steps.Steps.*;
import static steps.Steps.createUser;

public class RegisterTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Create user with valid params")
  public void createUserWithValidParams() {
    String accessToken = createUser();

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Create user with invalid email")
  public void createUserWithInvalidEmail() {
    String email = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 500);
  }

  @Test
  @DisplayName("Create existing user")
  public void createExistingUser() {
    User user = new User("test-data@yandex.ru", "password", "Username");
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 403);
    checkResponseBody(response, "success", false);
    checkResponseBody(response, "message", "User already exists");
  }

  @Test
  @DisplayName("Create user without email")
  public void createUserWithoutEmail() {
    User user = new User("", "password", "Username");
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 403);
    checkResponseBody(response, "success", false);
    checkResponseBody(response, "message", "Email, password and name are required fields");
  }

  @Test
  @DisplayName("Create user without password")
  public void createUserWithoutPassword() {
    User user = new User("test-data@yandex.ru", "", "Username");
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 403);
    checkResponseBody(response, "success", false);
    checkResponseBody(response, "message", "Email, password and name are required fields");
  }

  @Test
  @DisplayName("Create user without name")
  public void createUserWithoutName() {
    User user = new User("test-data@yandex.ru", "password", "");
    Response response = doPostRequest(user, "/api/auth/register");
    checkStatusCode(response, 403);
    checkResponseBody(response, "success", false);
    checkResponseBody(response, "message", "Email, password and name are required fields");
  }
}