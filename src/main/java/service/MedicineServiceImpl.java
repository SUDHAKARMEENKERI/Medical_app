package service;

import dao.MedicineDao;
import modal.MedicineRequest;
import modal.MedicineResponse;
import org.springframework.stereotype.Service;

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
        return response;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
