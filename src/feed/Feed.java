package feed;

import java.util.ArrayList;
import java.util.Date;

/*Esta clase modela la lista de articulos de un determinado feed*/
public class Feed extends GlobalFeed {
	String siteName;

	public Feed(String siteName) {
		super();
		this.siteName = siteName;
		this.articleList = new ArrayList<Article>();
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Override
	public String toString() {
		return "Feed [siteName=" + getSiteName() + ", articleList=" + getArticleList() + "]";
	}

	public void prettyPrint() {
		for (Article a : this.getArticleList()) {
			a.prettyPrint();
		}
	}

	public static void main(String[] args) {
		Article a1 = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		Article a2 = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		Article a3 = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		Feed f = new Feed("nytimes");
		f.addArticle(a1);
		f.addArticle(a2);
		f.addArticle(a3);

		f.prettyPrint();

	}

}
