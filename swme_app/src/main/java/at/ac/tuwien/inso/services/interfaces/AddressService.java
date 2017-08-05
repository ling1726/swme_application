package at.ac.tuwien.inso.services.interfaces;


import at.ac.tuwien.inso.entities.Address;

/**
 * This service provides methods to create new or change existing
 * address entries.
 * @author Lingfan Gao
 */
public interface AddressService {

    /**
     * Stores a new created address into the database.
     * This method is used when inserting a new person (must have an address).
     * @param address the person's address to persist
     * @return id the unique identifier of the persisted address
     */
    public Long insertAddress(Address address);

    /**
     * Changes an address entry in the database, given an example address.
     * This method is used when inserting a new person.
     * @param address the person's address to persist
     * @return Address the persisted address
     */
    public Address updateAddress(Address address);
}
