package dao;

import modal.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerDao extends JpaRepository<CustomerRequest, Long> {

    @Query("""
	    SELECT c FROM CustomerRequest c
	    WHERE (:storeId IS NULL OR c.storeId = :storeId)
	      AND (:storeMobile IS NULL OR c.storeMobile = :storeMobile OR c.phone = :storeMobile)
	      AND (:email IS NULL OR LOWER(c.email) = LOWER(:email))
	    """)
    List<CustomerRequest> findByFilters(
	    @Param("storeId") Long storeId,
	    @Param("storeMobile") String storeMobile,
	    @Param("email") String email);
}