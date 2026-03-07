package dao;

import modal.MedicalStoreRegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedicalStoreRegistrationDao extends JpaRepository<MedicalStoreRegistrationRequest, Long> {

    boolean existsByEmail(String email);

    boolean existsByStoreMobile(String storeMobile);

    boolean existsByLicenseNo(String licenseNo);

    Optional<MedicalStoreRegistrationRequest> findByStoreMobile(String storeMobile);

    Optional<MedicalStoreRegistrationRequest> findByMobile(String mobile);

    Optional<MedicalStoreRegistrationRequest> findByEmailAndStoreMobile(String email, String storeMobile);

        @Query("SELECT medicalStore FROM MedicalStoreRegistrationRequest medicalStore " +
            "WHERE medicalStore.email = :email " +
            "AND (medicalStore.mobile = :mobile OR medicalStore.storeMobile = :mobile)")
        Optional<MedicalStoreRegistrationRequest> findByEmailAndAnyMobile(
            @Param("email") String email,
            @Param("mobile") String mobile);
}