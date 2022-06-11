package data;

import java.util.List;

public class TestData {
  private static List<String> ingredientKit1 = List.of(
          "61c0c5a71d1f82001bdaaa6d",
          "61c0c5a71d1f82001bdaaa6f"
  );

  private static List<String> ingredientKit2 = List.of(
          "61c0c5a71d1f82001bdaaa6d",
          "61c0c5a71d1f82001bdaaa71",
          "61c0c5a71d1f82001bdaaa72"
  );

  private static List<String> ingredientWithInvalidHash = List.of(
          "61c0c5a71d1f82001bdaaa6d1",
          "61c0c5a71d1f82001bdaaa6f1"
  );

  public static List<String> getIngredientKit1() {
    return ingredientKit1;
  }

  public static List<String> getIngredientKit2() {
    return ingredientKit2;
  }

  public static List<String> getIngredientWithInvalidHash() {
    return ingredientWithInvalidHash;
  }
}
