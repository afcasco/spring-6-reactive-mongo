package dev.afcasco.reactivemongo.mapper;

import dev.afcasco.reactivemongo.domain.Customer;
import dev.afcasco.reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);
}
