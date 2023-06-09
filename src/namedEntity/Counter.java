package namedEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import scala.Tuple2;

final class SubCounter {
  private Dictionary<String, Dictionary<String, Integer>> dict;
  private Integer total;

  public SubCounter() {
    dict = new Hashtable<>();
    total = 0;
  }

  public void increment(String key, String subKey) {
    increment(key, subKey, 1);
  }

  public void increment(String key, String subKey, int value) {
    var subDict = dict.get(key);

    if (subDict == null) {
      Dictionary<String, Integer> newDict = new Hashtable<>();
      newDict.put(subKey, 1);

      dict.put(key, newDict);
    } else {
      var subValue = subDict.get(subKey);
  
      if (subValue == null) {
        subValue = 0;
      }

      subDict.put(subKey, subValue + value);
    }

    total += value;
  }

  public Integer getTotal() {
    return total;
  }

  public Dictionary<String, List<Tuple2<String, Integer>>> getOrdered() {
    Dictionary<String, List<Tuple2<String, Integer>>> orderedDict = new Hashtable<>();
    
    Enumeration<String> k = dict.keys();
    while (k.hasMoreElements()) {
      String key = k.nextElement();
      var subDict = dict.get(key);
      
      var list = new ArrayList<Tuple2<String, Integer>>();
      Enumeration<String> k2 = subDict.keys();
      while (k2.hasMoreElements()) {
        String key2 = k2.nextElement();
        var value = subDict.get(key2);

        list.add(new Tuple2<String, Integer>(key2, value));
      }

      list.sort((a, b) -> b._2.compareTo(a._2));
      orderedDict.put(key, list);
    }

    return orderedDict;
  }
}

public class Counter implements Serializable {
  private static Dictionary<String, SubCounter> dict = new Hashtable<>();

  public static void increment(String key, String subKey, String subSubKey) {
    SubCounter subCounter = dict.get(key);

    if (dict.get(key) == null) {
      subCounter = new SubCounter();
      dict.put(key, subCounter);
    }

    subCounter.increment(subKey, subSubKey);
  }

  public static void increment(String key, String subKey, String subSubKey, int value) {
    SubCounter subCounter = dict.get(key);

    if (dict.get(key) == null) {
      subCounter = new SubCounter();
      dict.put(key, subCounter);
    }

    subCounter.increment(subKey, subSubKey, value);
  }

  public static int getTotal(String key) {
    SubCounter r = dict.get(key);

    if (r == null)
      return 0;
    return r.getTotal();
  }

  public static Dictionary<String, List<Tuple2<String, Integer>>> getOrdered() {
    Dictionary<String, List<Tuple2<String, Integer>>> orderedDict = null;
    
    Enumeration<String> k = dict.keys();
    while (k.hasMoreElements()) {
      String key = k.nextElement();
      var subDict = dict.get(key).getOrdered();
      
      if (orderedDict == null) {
        orderedDict = subDict;
        continue;
      }

      Enumeration<String> k2 = subDict.keys();
      while (k2.hasMoreElements()) {
        String key2 = k2.nextElement();
        var list = subDict.get(key2);

        orderedDict.put(key2, list);
      }

    }

    return orderedDict;
  }
}
