package at.ac.tuwien.inso.services;

import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.repositories.interfaces.AddressRepository;
import at.ac.tuwien.inso.services.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
@Service
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public Long insertAddress(Address address) {
        return addressRepository.saveAndFlush(address).getId();
    }

    @Override
    @Transactional
    public Address updateAddress(Address address) {
        return addressRepository.saveAndFlush(address);
    }
}
