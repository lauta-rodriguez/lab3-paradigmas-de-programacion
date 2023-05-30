package parser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import subscription.SingleSubscription;
import subscription.Subscription;

/*
 * Esta clase implementa el parser del  archivo de suscripcion (json)
 * Leer https://www.w3docs.com/snippets/java/how-to-parse-json-in-java.html
 * */

public class SubscriptionParser extends GeneralParser<Subscription> {

    @Override
    public String getParserType() {
        return "subscription";
    }

    /*
     * Este metodo parsea un objeto json y devuelve un objeto SingleSubscription
     */
    private static SingleSubscription getSingleSubscription(JSONObject obj) {

        String url = obj.getString("url");
        String urlType = obj.getString("urlType");
        List<String> urlParams = new ArrayList<String>();

        JSONArray urlParamsJsonArray = obj.getJSONArray("urlParams");
        for (int j = 0; j < urlParamsJsonArray.length(); j++) {
            String urlParam = urlParamsJsonArray.getString(j);
            urlParams.add(urlParam);
        }
        SingleSubscription single = new SingleSubscription(url, urlParams, urlType);

        return single;
    }

    /*
     * Path al archivo de configuracion que contiene la lista de suscripciones
     * en formato json
     */
    public Subscription parse(String path) {
        Subscription subscription = new Subscription(path);

        try {
            FileReader reader = new FileReader(path);
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                SingleSubscription single = getSingleSubscription(obj);
                subscription.addSingleSubscription(single);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscription;
    }
}