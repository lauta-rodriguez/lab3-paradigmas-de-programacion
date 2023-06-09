package namedEntity;

import java.io.Serializable;

import topic.Topic;

/*Esta clase modela la nocion de entidad nombrada*/

public class NamedEntity implements Serializable {
	String name;
	private String category = "Other";
	private String parentCategory = "Named Entity";

	private static int frequency = 0;
	private int neFrequency = 0;
	private String origin = "";

	Topic topic;

	public NamedEntity(String name) {
		super();
		this.name = name;
		frequency++;
		incrementNEFrequency();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	protected void setCategory(String category) {
		this.category = category;
	}

	public static int getFrequency() {
		return frequency;
	}

	public int getNEFrequency() {
		return neFrequency;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void incrementFrequency() {
		frequency++;
		incrementNEFrequency();
		getTopic().incrementFrequency();
	}

	public void incrementNEFrequency() {
		neFrequency++;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	protected void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	protected String getParentCategory() {
		return this.parentCategory;
	}

	@Override
	public String toString() {
		return "ObjectNamedEntity [name=" + name + ", frequency=" + frequency + "]";
	}

	public String StringifyObject() {
		return ("[(" + getName() + ", " + getNEFrequency() + "): (" + getCategory() + ", " + getFrequency() + ") ");
	}

	public void prettyPrint() {
		System.out.println(this.StringifyObject() + " " + this.getTopic().StringifyObject());
	}

}
