package feed;

import java.util.ArrayList;
import java.util.List;

public class GlobalFeed {
	List<Article> articleList;

	public GlobalFeed() {
		super();
		this.articleList = new ArrayList<Article>();
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public void appendArticleList(List<Article> articleList) {
		this.articleList.addAll(articleList);
	}

	public void addArticle(Article a) {
		this.getArticleList().add(a);
	}

	public Article getArticle(int i) {
		return this.getArticleList().get(i);
	}

	public int getNumberOfArticles() {
		return this.getArticleList().size();
	}

	public void prettyPrint() {
		for (Article a : this.getArticleList()) {
			a.prettyPrint();
		}
	}

}
