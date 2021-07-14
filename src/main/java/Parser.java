import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class Parser {

    private final int PRODUCTS_QUANTITY;
    private final int STEP;
    private final String URL;

    public Parser(int PRODUCTS_QUANTITY, int STEP, String URL) {
        this.PRODUCTS_QUANTITY = PRODUCTS_QUANTITY;
        this.STEP = STEP;
        this.URL = URL;
    }

    private String deriveJson(String responseLine){
        int startIndex = responseLine.indexOf('{');
        int lastIndex = responseLine.lastIndexOf('}') + 1;
        return responseLine.substring(startIndex, lastIndex);
    }

    private List<Product> parseResponseLine(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode results = objectMapper.readTree(jsonResponse).get("results");
        String resultsString = results.toString();
        return objectMapper.readValue(resultsString, new TypeReference<List<Product>>() {
        });
    }

    public List<Product> getProductsPortion(int STEP, int index) {

        String url = String.format(URL, STEP, index);
        HttpGet httpGetRequest = new HttpGet(url);

        try (CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpGetRequest)) {
                HttpEntity responseHttpEntity = response.getEntity();
                if (responseHttpEntity != null){
                    String responseLine = EntityUtils.toString(responseHttpEntity);
                    return parseResponseLine(deriveJson(responseLine));
                }
            } catch (IOException ex){
            ex.printStackTrace();
        }

        return Collections.emptyList();
    }


    public List<Product> getProducts(){
        List<Product> productList = new ArrayList<>();
        for (int index = 0; index < PRODUCTS_QUANTITY; index += STEP) {
            productList.addAll(getProductsPortion(STEP, index));
        }
        return productList;
    }
}
