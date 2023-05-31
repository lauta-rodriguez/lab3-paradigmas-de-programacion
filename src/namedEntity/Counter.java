package namedEntity;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class Counter implements Serializable {
  private static Dictionary<String, Integer> dict = new Hashtable<>();

  public static void increment(String key) {
    if (dict.get(key) == null) {
      dict.put(key, 1);
    } else {
      dict.put(key, dict.get(key) + 1);
    }
  }

  public static void increment(String key, int value) {
    if (dict.get(key) == null) {
      dict.put(key, value);
    } else {
      dict.put(key, dict.get(key) + value);
    }
  }

  public static int get(String key) {
    Integer r = dict.get(key);

    if (r == null) return 0;
    return r;
  }
}
