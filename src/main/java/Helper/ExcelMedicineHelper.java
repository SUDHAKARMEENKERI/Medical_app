package Helper;

import modal.MedicineRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

public class ExcelMedicineHelper {
    public static List<MedicineRequest> parse(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) {
                throw new RuntimeException("Excel sheet is empty");
            }

            // Read header row
            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = formatter.formatCellValue(cell).trim().toLowerCase();
                headerMap.put(header, cell.getColumnIndex());
            }

            // Example required columns (customize as needed)
            String[] requiredCol = {
                "name", "brand", "composition", "category", "batch", "expiry", "quantity", "price", "storemobile", "storeid", "email"
            };
            for (String col : requiredCol) {
                if (!headerMap.containsKey(col)) {
                    throw new RuntimeException("Missing Excel column: " + col);
                }
            }

            List<MedicineRequest> list = new ArrayList<>();
            while (rows.hasNext()) {
                Row row = rows.next();
                MedicineRequest req = new MedicineRequest();
                req.setName(formatter.formatCellValue(row.getCell(headerMap.get("name"))));
                req.setBrand(formatter.formatCellValue(row.getCell(headerMap.get("brand"))));
                req.setComposition(formatter.formatCellValue(row.getCell(headerMap.get("composition"))));
                req.setCategory(formatter.formatCellValue(row.getCell(headerMap.get("category"))));
                req.setBatch(formatter.formatCellValue(row.getCell(headerMap.get("batch"))));
                String expiryStr = formatter.formatCellValue(row.getCell(headerMap.get("expiry")));
                if (!expiryStr.isEmpty()) {
                    try {
                        // Try ISO format first
                        req.setExpiry(java.time.LocalDate.parse(expiryStr));
                    } catch (java.time.format.DateTimeParseException e) {
                        // Try common Excel date formats
                        java.time.format.DateTimeFormatter formatter1 = java.time.format.DateTimeFormatter.ofPattern("M/d/yy");
                        java.time.format.DateTimeFormatter formatter2 = java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy");
                        try {
                            req.setExpiry(java.time.LocalDate.parse(expiryStr, formatter1));
                        } catch (java.time.format.DateTimeParseException e2) {
                            try {
                                req.setExpiry(java.time.LocalDate.parse(expiryStr, formatter2));
                            } catch (java.time.format.DateTimeParseException e3) {
                                throw new RuntimeException("Invalid expiry date format: " + expiryStr);
                            }
                        }
                    }
                }
                String quantityStr = formatter.formatCellValue(row.getCell(headerMap.get("quantity")));
                if (!quantityStr.isEmpty()) {
                    req.setQuantity(Integer.parseInt(quantityStr));
                }
                String priceStr = formatter.formatCellValue(row.getCell(headerMap.get("price")));
                if (!priceStr.isEmpty()) {
                    req.setPrice(new java.math.BigDecimal(priceStr));
                }
                req.setStoreMobile(formatter.formatCellValue(row.getCell(headerMap.get("storemobile"))));
                String storeIdStr = formatter.formatCellValue(row.getCell(headerMap.get("storeid")));
                if (!storeIdStr.isEmpty()) {
                    req.setStoreId(Long.parseLong(storeIdStr));
                }
                req.setEmail(formatter.formatCellValue(row.getCell(headerMap.get("email"))));
                // Add more fields as needed
                list.add(req);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Excel file: " + e.getMessage(), e);
        }
    }
}
