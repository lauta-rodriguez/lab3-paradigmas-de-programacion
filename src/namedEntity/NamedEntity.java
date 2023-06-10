package namedEntity;

import topic.Topic;
import java.io.Serializable;

/*Esta clase modela la nocion de entidad nombrada*/

public class NamedEntity implements Serializable {
	String name;
	private String category = "Other";
	private String parentCategory = "Named Entity";
	private String articleLink = "";

	private static int frequency = 0;

	Topic topic;

	public NamedEntity(String name) {
		super();
		this.name = name;
		frequency++;
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

	public String getArticleLink() {
		return this.articleLink;
	}

	public void setArticleLink(String articleLink) {
		this.articleLink = articleLink;
	}

	public void incrementFrequency() {
		frequency++;
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
		return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
	}

	public void prettyPrint() {
		System.out.println(this.StringifyObject() + " " + this.getTopic().StringifyObject());
	}

}
