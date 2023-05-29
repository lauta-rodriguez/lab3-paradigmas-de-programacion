package namedEntity.classes.Event;

import java.util.Date;

public class Event extends namedEntity.NamedEntity {

  // forma canónica, fecha, recurrente
  private String canonicalForm;
  private Date date;
  private boolean recurrent;

  private static int totalFrequency = 0;

  public Event(String name) {
    super(name);
    this.setCategory("Event");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean isRecurrent() {
    return recurrent;
  }

  public void setRecurrent(boolean recurrent) {
    this.recurrent = recurrent;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }

}
