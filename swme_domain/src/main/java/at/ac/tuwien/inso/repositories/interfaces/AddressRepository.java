package at.ac.tuwien.inso.repositories.interfaces;

import at.ac.tuwien.inso.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides query methods for CRUD operations on Addresses.
 *
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}
