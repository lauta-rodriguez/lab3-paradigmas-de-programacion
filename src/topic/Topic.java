package topic;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import namedEntity.heuristic.Heuristic;
import topic.Culture.*;
import topic.Politics.*;
import topic.Sports.*;

public class Topic {
  private String name = "-";
  private String category = "Other";
  private String parentCategory = "Topic";

  private static int frequency = 0;

  // dictionary used to map a topic to the corresponding subclass
  private static final Map<String, Class<? extends Topic>> TOPIC_CLASS_MAP = new HashMap<>();

  public Topic(String name) {
    this.name = name;
    frequency++;
  }

  public static int getFrequency() {
    return frequency;
  }

  public void incrementFrequency() {
		frequency++;
	}

  public String getName() {
    return this.name;
  }

  public String getCategory() {
    return this.category;
  }

  protected void setCategory(String category) {
    this.category = category;
  }

  public String getParentCategory() {
    return this.parentCategory;
  }

  protected void setParentCategory(String parentCategory) {
    this.parentCategory = parentCategory;
  }

  public static Topic generateTopic(String namedEntity, int frequency)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SecurityException, ClassNotFoundException {

    String topic = Heuristic.getTopic(namedEntity);

    Class<? extends Topic> t_action = TOPIC_CLASS_MAP.getOrDefault(topic,
        Topic.class);
    Topic t = t_action.getDeclaredConstructor(String.class,
        int.class).newInstance(topic, frequency);

    return t;
  }

  static {
    TOPIC_CLASS_MAP.put("Culture", Culture.class);
    TOPIC_CLASS_MAP.put("Cine", Cine.class);
    TOPIC_CLASS_MAP.put("Music", Music.class);
    TOPIC_CLASS_MAP.put("Politics", Politics.class);
    TOPIC_CLASS_MAP.put("International", International.class);
    TOPIC_CLASS_MAP.put("National", National.class);
    TOPIC_CLASS_MAP.put("Sports", Sports.class);
    TOPIC_CLASS_MAP.put("Futbol", Futbol.class);
    TOPIC_CLASS_MAP.put("Basket", Basket.class);
    TOPIC_CLASS_MAP.put("Tennis", Tennis.class);
    TOPIC_CLASS_MAP.put("F1", F1.class);
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }

  public static void main(String[] args) {
    Topic topic = new Topic("name");
    System.out.println(topic.StringifyObject());
  }

}
