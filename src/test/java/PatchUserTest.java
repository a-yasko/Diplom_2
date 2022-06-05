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

public class PatchUserTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Change user email")
  public void changeUserEmail() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response register = doPostRequest(user, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);
    String accessToken = register.path("accessToken");

    User userWithNewEmail = new User("123" + email, password, name);
    Response patchUser = doPatchRequest(userWithNewEmail, "/api/auth/user", accessToken);
    checkStatusCode(patchUser, 200);
    checkResponseBody(patchUser, "success", true);
    checkResponseBody(patchUser, "user.email", "123" + email.toLowerCase());

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Change user password")
  public void changeUserPassword() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response register = doPostRequest(user, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);
    String accessToken = register.path("accessToken");

    User userWithNewPassword = new User(email, "123" + password, name);
    Response patchUser = doPatchRequest(userWithNewPassword, "/api/auth/user", accessToken);
    checkStatusCode(patchUser, 200);
    checkResponseBody(patchUser, "success", true);

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Change user name")
  public void changeUserName() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response register = doPostRequest(user, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);
    String accessToken = register.path("accessToken");

    User userWithNewName = new User(email, password, "123" + name);
    Response patchUser = doPatchRequest(userWithNewName, "/api/auth/user", accessToken);
    checkStatusCode(patchUser, 200);
    checkResponseBody(patchUser, "success", true);
    checkResponseBody(patchUser, "user.name", "123" + name);

    deleteUser(accessToken);
  }

  @Test
  @DisplayName("Change user without token")
  public void changeUserWithoutToken() {
    User user = new User(
            RandomStringUtils.randomAlphabetic(10) + "@test.ru",
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(10)
    );
    Response response = doPatchRequest(user, "/api/auth/user", "");
    checkStatusCode(response, 401);
    checkResponseBody(response, "success", false);
  }

  @Test
  @DisplayName("Change user email to existing email")
  public void changeUserEmailToExistingEmail() {
    String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    User user = new User(email, password, name);
    Response register = doPostRequest(user, "/api/auth/register");
    checkStatusCode(register, 200);
    checkResponseBody(register, "success", true);
    String accessToken = register.path("accessToken");

    User userWithNewEmail = new User("test-data@yandex.ru", password, name);
    Response patchUser = doPatchRequest(userWithNewEmail, "/api/auth/user", accessToken);
    checkStatusCode(patchUser, 403);
    checkResponseBody(patchUser, "success", false);

    deleteUser(accessToken);
  }
}