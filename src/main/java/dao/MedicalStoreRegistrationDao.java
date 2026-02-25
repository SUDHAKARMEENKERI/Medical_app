package dao;

import modal.MedicalStoreRegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalStoreRegistrationDao extends JpaRepository<MedicalStoreRegistrationRequest, Long> {

    boolean existsByEmail(String email);

    boolean existsByStoreMobile(String storeMobile);

    boolean existsByLicenseNo(String licenseNo);

    Optional<MedicalStoreRegistrationRequest> findByStoreMobile(String storeMobile);
}