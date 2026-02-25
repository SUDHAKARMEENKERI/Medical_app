package dao;

import modal.MedicineRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineDao extends JpaRepository<MedicineRequest, Long> {

		@Query("""
				SELECT m FROM MedicineRequest m
				WHERE (:storeId IS NULL OR m.storeId = :storeId)
					AND (:storeMobile IS NULL OR m.storeMobile = :storeMobile)
					AND (:email IS NULL OR LOWER(m.email) = LOWER(:email))
				""")
		List<MedicineRequest> findByFilters(
						@Param("storeId") Long storeId,
						@Param("storeMobile") String storeMobile,
						@Param("email") String email);
}
