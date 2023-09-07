package dev.afcasco.reactivemongo.service;

import dev.afcasco.reactivemongo.domain.Customer;
import dev.afcasco.reactivemongo.mapper.CustomerMapper;
import dev.afcasco.reactivemongo.mapper.CustomerMapperImpl;
import dev.afcasco.reactivemongo.model.CustomerDTO;
import dev.afcasco.reactivemongo.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp()  {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Test Customer")
                .build();
    }

    public static CustomerDTO getTestCustomerDto(){
        return new CustomerMapperImpl().customerToCustomerDto(getTestCustomer());
    }

    public CustomerDTO getSavedCustomerDto(){
        return customerService.saveCustomer(customerDTO).block();
    }

    @Test
    void listCustomers() throws InterruptedException {
        customerService.saveCustomer(customerDTO).subscribe();
        customerService.listCustomers().subscribe(System.out::println);
        Thread.sleep(1000L);
    }

    @Test
    void findById() {
    }

    @Test
    void saveCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void patchCustomer() {
    }

    @Test
    void deleteById() {
    }
}