import com.fasterxml.jackson.dataformat.csv.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {

    private final String fileName;

    public CsvFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeFile (List<Product> productList) throws IOException {

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(Product.class);
        csvMapper.writer(csvSchema.withHeader().withColumnSeparator('-')).writeValue(new File(fileName), productList);
    }
}
