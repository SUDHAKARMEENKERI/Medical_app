
package service;

import dao.MedicineDao;
import modal.MedicineRequest;
import modal.MedicineResponse;
import org.springframework.stereotype.Service;

import Helper.ExcelMedicineHelper;

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
