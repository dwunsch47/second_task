import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static void main() {
        String url = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
        List<CatFact> allCatFacts = new ArrayList<>();
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                Type listType = new TypeToken<List<CatFact>>(){}.getType();
                Gson gson = new GsonBuilder().create();
                allCatFacts = gson.fromJson(EntityUtils.toString(response.getEntity()), listType);
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!allCatFacts.isEmpty()) {
            allCatFacts.stream().filter(fact -> fact.upvotes > 0).forEach(System.out::println);
        }
    }
}
