package namedEntity.classes.Person;

import namedEntity.NamedEntity;

public class Person extends NamedEntity {

  protected String id = "ID PERSON";

  private static int totalFrequency = 0;

  public Person(String name) {
    super(name);
    this.setCategory("Person");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
