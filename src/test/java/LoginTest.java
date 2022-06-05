import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static steps.Steps.*;
import static steps.Steps.deleteUser;

public class LoginTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Login")
  public void login() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response register = doPostRequest(user, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);

    Response login = doPostRequest(user, "/api/auth/login");
    checkStatusCode(login, 200);
    checkResponseBody(login, "success", true);

    String accessToken = login.path("accessToken");
    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Login with invalid credentials")
  public void loginWithInvalidCredentials() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response response = doPostRequest(user, "/api/auth/login");
    checkStatusCode(response, 401);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Login with empty email")
  public void loginWithEmptyEmail() {
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User("", password, name);
    Response response = doPostRequest(user, "/api/auth/login");
    checkStatusCode(response, 401);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Login with empty password")
  public void loginWithEmptyPassword() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, "", name);
    Response response = doPostRequest(user, "/api/auth/login");
    checkStatusCode(response, 401);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Login with empty name")
  public void loginWithEmptyName() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User userWithName = new User(email, password, name);
    Response register = doPostRequest(userWithName, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);

    User userWithoutName = new User(email, password, "");
    Response login = doPostRequest(userWithoutName, "/api/auth/login");
    checkStatusCode(login, 200);
    checkResponseBody(login, "success", true);

    String accessToken = login.path("accessToken");
    deleteUser(accessToken);
  }
}