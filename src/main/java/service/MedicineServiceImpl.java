
package service;

import dao.MedicineDao;
import modal.MedicinePatchRequest;
import modal.MedicineRequest;
import modal.MedicineResponse;
import org.springframework.stereotype.Service;

import Helper.ExcelMedicineHelper;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineDao medicineDao;

    public MedicineServiceImpl(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
    }

    @Override
    public MedicineResponse addMedicine(MedicineRequest request) {
        request.setId(null);
        MedicineRequest savedMedicine = medicineDao.save(request);
        return toResponse(savedMedicine);
    }

    @Override
    public MedicineResponse patchMedicine(Long medicineId, MedicinePatchRequest request) {
        MedicineRequest existingMedicine = medicineDao.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));

        boolean anyFieldUpdated = false;

        if (request.getName() != null) {
            existingMedicine.setName(request.getName());
            anyFieldUpdated = true;
        }
        if (request.getBrand() != null) {
            existingMedicine.setBrand(request.getBrand());
            anyFieldUpdated = true;
        }
        if (request.getComposition() != null) {
            existingMedicine.setComposition(request.getComposition());
            anyFieldUpdated = true;
        }
        if (request.getCategory() != null) {
            existingMedicine.setCategory(request.getCategory());
            anyFieldUpdated = true;
        }
        if (request.getBatch() != null) {
            existingMedicine.setBatch(request.getBatch());
            anyFieldUpdated = true;
        }
        if (request.getExpiry() != null) {
            existingMedicine.setExpiry(request.getExpiry());
            anyFieldUpdated = true;
        }
        if (request.getQuantity() != null) {
            if (request.getQuantity() < 0) {
                throw new IllegalArgumentException("quantity must be 0 or greater");
            }
            existingMedicine.setQuantity(request.getQuantity());
            anyFieldUpdated = true;
        }
        if (request.getPrice() != null) {
            if (request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("price must be 0 or greater");
            }
            existingMedicine.setPrice(request.getPrice());
            anyFieldUpdated = true;
        }
        if (request.getStoreMobile() != null) {
            existingMedicine.setStoreMobile(request.getStoreMobile());
            anyFieldUpdated = true;
        }
        if (request.getStoreId() != null) {
            existingMedicine.setStoreId(request.getStoreId());
            anyFieldUpdated = true;
        }
        if (request.getEmail() != null) {
            existingMedicine.setEmail(request.getEmail());
            anyFieldUpdated = true;
        }
        if (request.getFormulation() != null) {
            existingMedicine.setFormulation(request.getFormulation());
            anyFieldUpdated = true;
        }
        if (request.getStrength() != null) {
            existingMedicine.setStrength(request.getStrength());
            anyFieldUpdated = true;
        }
        if (request.getMfgDate() != null) {
            existingMedicine.setMfgDate(request.getMfgDate());
            anyFieldUpdated = true;
        }
        if (request.getPackSize() != null) {
            existingMedicine.setPackSize(request.getPackSize());
            anyFieldUpdated = true;
        }
        if (request.getBoxQuantity() != null) {
            existingMedicine.setBoxQuantity(request.getBoxQuantity());
            anyFieldUpdated = true;
        }
        if (request.getLowAlert() != null) {
            existingMedicine.setLowAlert(request.getLowAlert());
            anyFieldUpdated = true;
        }
        if (request.getRackShelf() != null) {
            existingMedicine.setRackShelf(request.getRackShelf());
            anyFieldUpdated = true;
        }
        if (request.getBuyPrice() != null) {
            existingMedicine.setBuyPrice(request.getBuyPrice());
            anyFieldUpdated = true;
        }
        if (request.getBoxBuyPrice() != null) {
            existingMedicine.setBoxBuyPrice(request.getBoxBuyPrice());
            anyFieldUpdated = true;
        }
        if (request.getBoxSellPrice() != null) {
            existingMedicine.setBoxSellPrice(request.getBoxSellPrice());
            anyFieldUpdated = true;
        }
        if (request.getGst() != null) {
            existingMedicine.setGst(request.getGst());
            anyFieldUpdated = true;
        }
        if (request.getManufacturer() != null) {
            existingMedicine.setManufacturer(request.getManufacturer());
            anyFieldUpdated = true;
        }
        if (request.getSupplier() != null) {
            existingMedicine.setSupplier(request.getSupplier());
            anyFieldUpdated = true;
        }
        if (request.getBatchSize() != null) {
            existingMedicine.setBatchSize(request.getBatchSize());
            anyFieldUpdated = true;
        }

        if (!anyFieldUpdated) {
            throw new IllegalArgumentException("At least one field must be provided");
        }

        MedicineRequest updatedMedicine = medicineDao.save(existingMedicine);
        return toResponse(updatedMedicine);
    }

    @Override
    public void deleteMedicine(Long medicineId) {
        MedicineRequest existingMedicine = medicineDao.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        medicineDao.delete(existingMedicine);
    }

    @Override
    public List<MedicineResponse> getAllMedicines(Long storeId, String storeMobile, String email) {
        return medicineDao.findByFilters(storeId, normalize(storeMobile), normalize(email)).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<MedicineResponse> bulkUploadMedicines(List<MedicineRequest> requests) {
        List<MedicineRequest> savedMedicines = medicineDao.saveAll(requests.stream().peek(r -> r.setId(null)).toList());
        return savedMedicines.stream().map(this::toResponse).toList();
    }

    private MedicineResponse toResponse(MedicineRequest medicine) {
        MedicineResponse response = new MedicineResponse();
        response.setId(medicine.getId());
        response.setName(medicine.getName());
        response.setBrand(medicine.getBrand());
        response.setComposition(medicine.getComposition());
        response.setCategory(medicine.getCategory());
        response.setBatch(medicine.getBatch());
        response.setExpiry(medicine.getExpiry());
        response.setQuantity(medicine.getQuantity());
        response.setPrice(medicine.getPrice());
        response.setStoreMobile(medicine.getStoreMobile());
        response.setStoreId(medicine.getStoreId());
        response.setEmail(medicine.getEmail());
        response.setFormulation(medicine.getFormulation());
        response.setStrength(medicine.getStrength());
        response.setMfgDate(medicine.getMfgDate());
        response.setPackSize(medicine.getPackSize());
        response.setBoxQuantity(medicine.getBoxQuantity());
        response.setLowAlert(medicine.getLowAlert());
        response.setRackShelf(medicine.getRackShelf());
        response.setBuyPrice(medicine.getBuyPrice());
        response.setBoxBuyPrice(medicine.getBoxBuyPrice());
        response.setBoxSellPrice(medicine.getBoxSellPrice());
        response.setGst(medicine.getGst());
        response.setManufacturer(medicine.getManufacturer());
        response.setSupplier(medicine.getSupplier());
        response.setBatchSize(medicine.getBatchSize());
        return response;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    public int bulkUploadExcel(org.springframework.web.multipart.MultipartFile excelFile) {
        if (excelFile.isEmpty()) {
            throw new RuntimeException("Upload file is empty");
        }
        try {
            List<MedicineRequest> list = ExcelMedicineHelper.parse(
                    excelFile.getInputStream(),
                    excelFile.getOriginalFilename(),
                    excelFile.getContentType());
            List<MedicineRequest> saved = medicineDao.saveAll(list.stream().peek(r -> r.setId(null)).toList());
            return saved.size();
        } catch (Exception e) {
            throw new RuntimeException("Bulk upload failed: " + e.getMessage(), e);
        }
    }
}
