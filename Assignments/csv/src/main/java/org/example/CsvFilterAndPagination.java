package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFilterAndPagination {

    public List<String[]> filterAndPaginate(String csvFilePath, String filterExpression, int pageNumber) throws IOException {
        List<String[]> rows = readCsvFile(csvFilePath);
        List<String[]> filteredRows = filterRows(rows, filterExpression);
        List<String[]> paginatedRows = paginateRows(filteredRows, pageNumber);
        return paginatedRows;
    }

    private List<String[]> readCsvFile(String csvFilePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                rows.add(row);
            }
        }
        return rows;
    }

    private List<String[]> filterRows(List<String[]> rows, String filterExpression) {
        List<String[]> filteredRows = new ArrayList<>();
        String[] tokens = filterExpression.split("and|or");
        for (String[] row : rows) {
            boolean match = true;
            for (String token : tokens) {
                String[] parts = token.split("=");
                if (parts.length == 2) {
                    String columnName = parts[0].trim();
                    String columnValue = parts[1].trim();
                    if (columnName.equals("column_name")) {
                        match = match && (row[1].equals(columnValue) || (columnValue.equals("100") && Integer.parseInt(row[1]) <= 100));
                    } else if (columnName.equals("column_name") && columnValue.equals("'practo'")) {
                        match = match && row[2].equals(columnValue);
                    } else if (columnName.equals("column_name") && columnValue.equals("'dogreat'")) {
                        match = match && !row[2].equals(columnValue);
                    }
                }
            }
            if (match) {
                filteredRows.add(row);
            }
        }
        return filteredRows;
    }

    private List<String[]> paginateRows(List<String[]> rows, int pageNumber) {
        int pageSize = 5; // You can adjust the page size as needed
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, rows.size());
        return rows.subList(startIndex, endIndex);
    }

    public static void main(String[] args) throws IOException {
        CsvFilterAndPagination csvFilter = new CsvFilterAndPagination();
        String csvFilePath = "/Users/challaabhishek/Documents/read.csv";
        String filterExpression = "(((column_name = 'practo') and (column_name != 'dogreat')) or (column_name <= 100))";
        int pageNumber = 1;
        List<String[]> paginatedRows = csvFilter.filterAndPaginate(csvFilePath, filterExpression, pageNumber);
        for (String[] row : paginatedRows) {
            System.out.println(String.join(",", row));
        }
    }
}
