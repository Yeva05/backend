package dev.vorstu.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.vorstu.models.dto.RegistrationCsvRow;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class CsvParser {
    private final CsvMapper csvMapper = new CsvMapper();

    public List<RegistrationCsvRow> parseCsv(InputStream inputStream) throws IOException {
        CsvSchema schema = csvMapper.schemaFor(RegistrationCsvRow.class)
                .withHeader()
                .withColumnReordering(true);

        MappingIterator<RegistrationCsvRow> iterator = csvMapper
                .readerFor(RegistrationCsvRow.class)
                .with(schema)
                .readValues(inputStream);

        return iterator.readAll();
    }
}
