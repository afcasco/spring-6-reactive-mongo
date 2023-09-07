package dev.afcasco.reactivemongo.service;

import dev.afcasco.reactivemongo.domain.Beer;
import dev.afcasco.reactivemongo.mapper.BeerMapper;
import dev.afcasco.reactivemongo.mapper.BeerMapperImpl;
import dev.afcasco.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void listBeers() {


    }

    @Test
    @DisplayName("Test Save Beer Using Subscribe")
    void testSaveBeer() {

        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveBeer(beerDTO);

        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            assertThat(savedDto).isNotNull();
            atomicDto.set(savedDto);
        });

        await().until(()->atomicDto.get()!=null);
        BeerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    void testGetById() {
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void updateBeer() {
        final String newName = "New Beer Name";

        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        beerService.saveBeer(getSavedBeerDto()).map( savedBeerDto ->  {
            savedBeerDto.setBeerName(newName);
            return savedBeerDto;
        })
                .flatMap(updatedDto -> beerService.updateBeer(updatedDto.getId(),updatedDto))
                .flatMap(saveUpdatedDto -> beerService.getById(saveUpdatedDto.getId()))
                .subscribe(atomicDto::set);

        await().until(()-> atomicDto.get() != null);
        assertThat(atomicDto.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void patchBeer() {


    }



    @Test
    void deleteBeerById() {
        BeerDTO beerToDelete = getSavedBeerDto();
        beerService.deleteBeerById(beerToDelete.getId()).block();
        Mono<BeerDTO> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());

        BeerDTO emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();
    }

    @Test
    void findFirstByBeerNameTest() {

        BeerDTO beerDTO = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> foundDto = beerService.findFirstByBeerName(beerDTO.getBeerName());

        foundDto.subscribe(dto -> {
            System.out.println(dto.getBeerName());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testFindByBeerStyle() {
        AtomicBoolean flag = new AtomicBoolean(false);
        beerService.findAllByBeerStyle(getTestBeerDto().getBeerStyle()).subscribe(dto -> {
            System.out.println(dto);
            flag.set(true);
        });

        await().untilTrue(flag);
    }

    public BeerDTO getSavedBeerDto(){
        return beerService.saveBeer(getTestBeerDto()).block();
    }

    private static BeerDTO getTestBeerDto() {
        return new BeerMapperImpl().beerToBeerDto(getTestBeer());
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(17)
                .upc("123456")
                .build();
    }
}