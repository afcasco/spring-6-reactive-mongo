package dev.afcasco.reactivemongo.bootstrap;

import dev.afcasco.reactivemongo.domain.Beer;
import dev.afcasco.reactivemongo.domain.Customer;
import dev.afcasco.reactivemongo.repository.BeerRepository;
import dev.afcasco.reactivemongo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll().doOnSuccess(success->loadBeerData())
                .subscribe();

        customerRepository.deleteAll().doOnSuccess(success -> loadCustomerData())
                .subscribe();
    }

    private void loadCustomerData() {

        customerRepository.count().subscribe(count -> {
            if(count ==0) {
                Customer customer1 = Customer.builder()
                        .customerName("Alex")
                        .build();

                Customer customer2 = Customer.builder()
                        .customerName("John")
                        .build();

                Customer customer3 = Customer.builder()
                        .customerName("Mary")
                        .build();

                customerRepository.saveAll(Arrays.asList(customer1,customer2,customer3))
                        .subscribe();
            }
        });
    }

    private void loadBeerData() {

        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("Pale Ale")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("Pale Ale")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
