package subscription;

import java.util.List;

/*Esta clse abstrae el contenido de una sola suscripcion que ocurre en lista de suscripciones que figuran en el archivo de suscrpcion(json) */
public class SingleSubscription {

	private String url;
	private List<String> UrlParams;
	private String urlType;

	public SingleSubscription(String url, List<String> UrlParams, String urlType) {
		super();
		this.url = url;
		this.UrlParams = UrlParams;
		this.urlType = urlType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getUrlParams() {
		return UrlParams;
	}

	public String getUrlParams(int i) {
		return this.UrlParams.get(i);
	}

	public void setUrlParams(String urlParam) {
		this.UrlParams.add(urlParam);
	}

	public int getUrlParamsSize() {
		return UrlParams.size();
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	@Override
	public String toString() {
		return "{url=" + getUrl() + ", UrlParams=" + getUrlParams().toString() + ", urlType=" + getUrlType() + "}";
	}

	public void prettyPrint() {
		System.out.println(this.toString());
	}

	public String getFeedToRequest(int i) {
		return this.getUrl().replace("%s", this.getUrlParams(i));
	}

	public static void main(String[] args) {
		System.out.println("SingleSubscriptionClass");
		SingleSubscription s = new SingleSubscription("https://rss.nytimes.com/services/xml/rss/nyt/%s.xml", null,
				"rss");
		s.setUrlParams("Business");
		s.setUrlParams("Technology");
		System.out.println(s.getFeedToRequest(0));
		s.prettyPrint();
	}

}
