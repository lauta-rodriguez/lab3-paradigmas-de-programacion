package namedEntity.classes.Person;

import namedEntity.NamedEntity;

public class Person extends NamedEntity {

  protected String id = "ID PERSON";

  private static int totalFrequency = 0;

  public Person(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Person");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
