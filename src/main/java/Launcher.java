import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Launcher {

    public static final int PRODUCTS_QUANTITY = 100;
    public static final int STEP = 5;
    public static final String FILE_NAME = "result.csv";
    public static final String PREPARED_ALIEXPRESS_URL = "https://gpsfront.aliexpress.com/getRecommendingResults.do?callback=jQuery18302554242081162217_1618820833082&widget_id=5547572&platform=pc&limit=%d&offset=%d&phase=1&productIds2Top=&postback=3dda66f9-7831-4a4d-aae1-984c02fabe36&_=1618820834619";

    public static void main(String[] args) throws IOException {

        Date start = new Date();

        Parser parser = new Parser(PRODUCTS_QUANTITY, STEP, PREPARED_ALIEXPRESS_URL);
        List<Product> productList = parser.getProducts();
        CsvFileWriter csvFileWriter = new CsvFileWriter(FILE_NAME);
        csvFileWriter.writeFile(productList);

        Date end = new Date();

        long performingTime = (end.getTime() - start.getTime()) / 1_000;

        System.out.println("Our overall parsing process took " + performingTime + " sec.");

    }
}