package namedEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import scala.Tuple2;

final class SubCounter {
  private Dictionary<String, Integer> dict;
  private Integer total;

  public SubCounter() {
    dict = new Hashtable<>();
    total = 0;
  }

  public void increment(String key) {
    if (dict.get(key) == null) {
      dict.put(key, 1);
    } else {
      dict.put(key, dict.get(key) + 1);
    }

    total++;
  }

  public void increment(String key, int value) {
    if (dict.get(key) == null) {
      dict.put(key, value);
    } else {
      dict.put(key, dict.get(key) + value);
    }

    total += value;
  }

  public Integer getTotal() {
    return total;
  }

  public List<Tuple2<String, Integer>> getOrdered() {
    var list = new ArrayList<Tuple2<String, Integer>>();

    Enumeration<String> k = dict.keys();
    while (k.hasMoreElements()) {
      String key = k.nextElement();
      list.add(new Tuple2<String, Integer>(key, dict.get(key)));
      System.out.println("\t\t\t\t" + key + ": " + dict.get(key));
    }

    return list;
  }
}

public class Counter implements Serializable {
  private static Dictionary<String, SubCounter> dict = new Hashtable<>();

  public static void increment(String key, String subKey) {
    SubCounter subCounter = dict.get(key);

    if (dict.get(key) == null) {
      subCounter = new SubCounter();
      dict.put(key, subCounter);
    }

    subCounter.increment(subKey);
  }

  public static void increment(String key, String subKey, int value) {
    SubCounter subCounter = dict.get(key);

    if (dict.get(key) == null) {
      subCounter = new SubCounter();
      dict.put(key, subCounter);
    }

    subCounter.increment(subKey, value);
  }

  public static int getTotal(String key) {
    SubCounter r = dict.get(key);

    if (r == null)
      return 0;
    return r.getTotal();
  }

  public static List<Tuple2<String, Integer>> getOrdered(String key) {
    SubCounter r = dict.get(key);

    if (r == null)
      return new ArrayList<>();
    return r.getOrdered();
  }
}
