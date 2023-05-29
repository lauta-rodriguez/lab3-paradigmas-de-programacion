package namedEntity.classes.Event;

import java.util.Date;

public class Event extends namedEntity.NamedEntity {

  // forma canónica, fecha, recurrente
  private String canonicalForm;
  private Date date;
  private boolean recurrent;

  private static int totalFrequency = 0;

  public Event(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Event");
    totalFrequency += frequency;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public Date getDate() {
    return date;
  }

  public boolean isRecurrent() {
    return recurrent;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setRecurrent(boolean recurrent) {
    this.recurrent = recurrent;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }

}
