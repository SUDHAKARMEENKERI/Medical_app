package Helper;

import modal.MedicineRequest;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ExcelMedicineHelper {
    private static final String[] REQUIRED_COLUMNS = {
            "name", "brand", "composition", "category", "batch", "expiry", "quantity", "price", "storemobile", "storeid", "email"
    };

    public static List<MedicineRequest> parse(InputStream is, String fileName, String contentType) {
        try {
            byte[] payload = is.readAllBytes();
            if (isLikelyCsv(fileName, contentType)) {
                return parseCsv(new ByteArrayInputStream(payload));
            }
            try {
                return parseExcel(new ByteArrayInputStream(payload));
            } catch (Exception excelError) {
                return parseCsv(new ByteArrayInputStream(payload));
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid upload file: " + e.getMessage(), e);
        }
    }

    public static List<MedicineRequest> parse(InputStream is) {
        return parse(is, null, null);
    }

    private static List<MedicineRequest> parseExcel(InputStream is) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) {
                throw new RuntimeException("Uploaded sheet is empty");
            }

            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = normalizeHeader(formatter.formatCellValue(cell));
                headerMap.put(header, cell.getColumnIndex());
            }

            applyCanonicalAliases(headerMap);
            validateRequiredColumns(headerMap);

            List<MedicineRequest> list = new ArrayList<>();
            while (rows.hasNext()) {
                Row row = rows.next();
                int rowNum = row.getRowNum() + 1;
                if (isRowEmpty(row, headerMap, formatter)) {
                    continue;
                }

                Map<String, String> values = new HashMap<>();
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    values.put(entry.getKey(), getCellString(row, entry.getValue(), formatter));
                }
                list.add(buildMedicine(values, rowNum));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Excel file: " + e.getMessage(), e);
        }
    }

    private static List<MedicineRequest> parseCsv(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new RuntimeException("CSV file is empty");
            }

            List<String> headers = parseCsvLine(stripBom(headerLine));
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                headerMap.put(normalizeHeader(headers.get(i)), i);
            }

            applyCanonicalAliases(headerMap);
            validateRequiredColumns(headerMap);

            List<MedicineRequest> list = new ArrayList<>();
            String line;
            int rowNum = 1;
            while ((line = reader.readLine()) != null) {
                rowNum++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> columns = parseCsvLine(line);
                Map<String, String> values = new HashMap<>();
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    values.put(entry.getKey(), getCsvValue(columns, entry.getValue()));
                }

                if (isValueRowEmpty(values)) {
                    continue;
                }
                list.add(buildMedicine(values, rowNum));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Invalid CSV file: " + e.getMessage(), e);
        }
    }

    private static boolean isRowEmpty(Row row, Map<String, Integer> headerMap, DataFormatter formatter) {
        for (Integer columnIndex : headerMap.values()) {
            if (columnIndex == null) {
                continue;
            }
            String value = formatter.formatCellValue(row.getCell(columnIndex));
            if (value != null && !value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static String getRequiredString(Row row, Map<String, Integer> headerMap, DataFormatter formatter, String column, int rowNum) {
        Integer index = headerMap.get(column);
        if (index == null) {
            throw new RuntimeException("Missing Excel column: " + column);
        }
        String value = formatter.formatCellValue(row.getCell(index));
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing required value for '" + column + "' at row " + rowNum);
        }
        return value.trim();
    }

    private static LocalDate parseLocalDate(Cell cell, DataFormatter formatter, int rowNum) {
        if (cell == null) {
            throw new RuntimeException("Missing required value for 'expiry' at row " + rowNum);
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String raw = formatter.formatCellValue(cell);
        if (raw == null || raw.trim().isEmpty()) {
            throw new RuntimeException("Missing required value for 'expiry' at row " + rowNum);
        }

        return parseLocalDateValue(raw.trim(), "expiry", rowNum);
    }

    private static Integer parseInteger(String value, String field, int rowNum) {
        try {
            return new BigDecimal(value.replace(",", "").trim()).intValueExact();
        } catch (Exception e) {
            throw new RuntimeException("Invalid " + field + " at row " + rowNum + ": " + value);
        }
    }

    private static Long parseLong(String value, String field, int rowNum) {
        try {
            return new BigDecimal(value.replace(",", "").trim()).longValueExact();
        } catch (Exception e) {
            throw new RuntimeException("Invalid " + field + " at row " + rowNum + ": " + value);
        }
    }

    private static BigDecimal parseBigDecimal(String value, String field, int rowNum) {
        try {
            return new BigDecimal(value.replace(",", "").trim());
        } catch (Exception e) {
            throw new RuntimeException("Invalid " + field + " at row " + rowNum + ": " + value);
        }
    }

    private static String getOptionalString(Row row, Map<String, Integer> headerMap, DataFormatter formatter, String column) {
        Integer index = headerMap.get(column);
        if (index == null) {
            return null;
        }
        String value = formatter.formatCellValue(row.getCell(index));
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }
        return header.trim().toLowerCase().replaceAll("[\\s_\\-]", "");
    }

    private static String getCellString(Row row, Integer index, DataFormatter formatter) {
        if (index == null) {
            return null;
        }
        String value = formatter.formatCellValue(row.getCell(index));
        return value == null ? null : value.trim();
    }

    private static String getCsvValue(List<String> columns, int index) {
        if (index < 0 || index >= columns.size()) {
            return null;
        }
        String value = columns.get(index);
        return value == null ? null : value.trim();
    }

    private static boolean isValueRowEmpty(Map<String, String> values) {
        for (String value : values.values()) {
            if (value != null && !value.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private static void applyCanonicalAliases(Map<String, Integer> headerMap) {
        mapCanonicalHeader(headerMap, "name", "name", "medicinename", "productname");
        mapCanonicalHeader(headerMap, "brand", "brand", "company", "brandname");
        mapCanonicalHeader(headerMap, "composition", "composition", "salt", "ingredients");
        mapCanonicalHeader(headerMap, "category", "category", "type");
        mapCanonicalHeader(headerMap, "batch", "batch", "batchno", "batchnumber");
        mapCanonicalHeader(headerMap, "expiry", "expiry", "expirydate", "expdate", "exp");
        mapCanonicalHeader(headerMap, "quantity", "quantity", "qty", "stock");
        mapCanonicalHeader(headerMap, "price", "price", "mrp", "sellprice", "sellingprice");
        mapCanonicalHeader(headerMap, "storemobile", "storemobile", "storemobileno", "mobile", "mobileno", "storephone");
        mapCanonicalHeader(headerMap, "storeid", "storeid", "shopid", "medicalstoreid", "store");
        mapCanonicalHeader(headerMap, "email", "email", "storeemail");

        mapCanonicalHeader(headerMap, "formulation", "formulation", "dosageform");
        mapCanonicalHeader(headerMap, "strength", "strength", "power");
        mapCanonicalHeader(headerMap, "mfgdate", "mfgdate", "manufacturingdate", "mfg");
        mapCanonicalHeader(headerMap, "packsize", "packsize", "pack", "packingsize");
        mapCanonicalHeader(headerMap, "boxquantity", "boxquantity", "boxqty");
        mapCanonicalHeader(headerMap, "lowalert", "lowalert", "lowstock", "lowstockalert", "reorderlevel");
        mapCanonicalHeader(headerMap, "rackshelf", "rackshelf", "rack", "shelf", "location");
        mapCanonicalHeader(headerMap, "buyprice", "buyprice", "purchaseprice");
        mapCanonicalHeader(headerMap, "boxbuyprice", "boxbuyprice", "boxpurchaseprice");
        mapCanonicalHeader(headerMap, "boxsellprice", "boxsellprice", "boxsellingprice");
        mapCanonicalHeader(headerMap, "gst", "gst", "tax");
        mapCanonicalHeader(headerMap, "manufacturer", "manufacturer", "mfgcompany");
        mapCanonicalHeader(headerMap, "supplier", "supplier", "vendor");
        mapCanonicalHeader(headerMap, "batchsize", "batchsize", "packqty", "stripcount");
    }

    private static void validateRequiredColumns(Map<String, Integer> headerMap) {
        for (String col : REQUIRED_COLUMNS) {
            if (!headerMap.containsKey(col)) {
                throw new RuntimeException("Missing required column: " + col);
            }
        }
    }

    private static MedicineRequest buildMedicine(Map<String, String> values, int rowNum) {
        MedicineRequest req = new MedicineRequest();
        req.setName(getRequiredValue(values, "name", rowNum));
        req.setBrand(getRequiredValue(values, "brand", rowNum));
        req.setComposition(getRequiredValue(values, "composition", rowNum));
        req.setCategory(getRequiredValue(values, "category", rowNum));
        req.setBatch(getRequiredValue(values, "batch", rowNum));
        req.setExpiry(parseLocalDateValue(getRequiredValue(values, "expiry", rowNum), "expiry", rowNum));
        req.setQuantity(parseInteger(getRequiredValue(values, "quantity", rowNum), "quantity", rowNum));
        req.setPrice(parseBigDecimal(getRequiredValue(values, "price", rowNum), "price", rowNum));
        req.setStoreMobile(getRequiredValue(values, "storemobile", rowNum));
        req.setStoreId(parseLong(getRequiredValue(values, "storeid", rowNum), "storeId", rowNum));
        req.setEmail(getRequiredValue(values, "email", rowNum));

        req.setFormulation(getOptionalValue(values, "formulation"));
        req.setStrength(getOptionalValue(values, "strength"));
        req.setMfgDate(getOptionalValue(values, "mfgdate"));
        req.setPackSize(getOptionalValue(values, "packsize"));
        req.setBoxQuantity(getOptionalValue(values, "boxquantity"));
        req.setLowAlert(getOptionalValue(values, "lowalert"));
        req.setRackShelf(getOptionalValue(values, "rackshelf"));
        req.setBuyPrice(getOptionalValue(values, "buyprice"));
        req.setBoxBuyPrice(getOptionalValue(values, "boxbuyprice"));
        req.setBoxSellPrice(getOptionalValue(values, "boxsellprice"));
        req.setGst(getOptionalValue(values, "gst"));
        req.setManufacturer(getOptionalValue(values, "manufacturer"));
        req.setSupplier(getOptionalValue(values, "supplier"));
        req.setBatchSize(getOptionalValue(values, "batchsize"));
        return req;
    }

    private static String getRequiredValue(Map<String, String> values, String key, int rowNum) {
        String value = values.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing required value for '" + key + "' at row " + rowNum);
        }
        return value.trim();
    }

    private static String getOptionalValue(Map<String, String> values, String key) {
        String value = values.get(key);
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static LocalDate parseLocalDateValue(String value, String field, int rowNum) {
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Missing required value for '" + field + "' at row " + rowNum);
        }

        String clean = value.trim();
        if (clean.matches("\\d+(\\.0+)?")) {
            try {
                int serial = new BigDecimal(clean).intValueExact();
                if (serial > 1000) {
                    return LocalDate.of(1899, 12, 30).plusDays(serial);
                }
            } catch (Exception ignored) {
            }
        }

        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("d/M/yy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(clean, format);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new RuntimeException("Invalid " + field + " format at row " + rowNum + ": " + clean);
    }

    private static boolean isLikelyCsv(String fileName, String contentType) {
        String lowerName = fileName == null ? "" : fileName.toLowerCase();
        String lowerContentType = contentType == null ? "" : contentType.toLowerCase();
        return lowerName.endsWith(".csv")
                || lowerContentType.contains("csv")
                || lowerContentType.contains("text/plain");
    }

    private static List<String> parseCsvLine(String line) {
        String safeLine = line == null ? "" : line;
        String[] parts = safeLine.split(",(?=(?:[^\"]*\\\"[^\"]*\\\")*[^\"]*$)", -1);
        List<String> values = new ArrayList<>(parts.length);
        for (String part : parts) {
            values.add(unquote(part));
        }
        return values;
    }

    private static String unquote(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            String body = trimmed.substring(1, trimmed.length() - 1);
            return body.replace("\"\"", "\"");
        }
        return trimmed;
    }

    private static String stripBom(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.charAt(0) == '\uFEFF' ? input.substring(1) : input;
    }

    private static void mapCanonicalHeader(Map<String, Integer> headerMap, String canonical, String... aliases) {
        if (headerMap.containsKey(canonical)) {
            return;
        }
        for (String alias : aliases) {
            Integer index = headerMap.get(normalizeHeader(alias));
            if (index != null) {
                headerMap.put(canonical, index);
                return;
            }
        }
    }
}
