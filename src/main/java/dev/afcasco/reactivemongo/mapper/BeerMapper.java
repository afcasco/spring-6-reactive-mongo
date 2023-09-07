package dev.afcasco.reactivemongo.mapper;

import dev.afcasco.reactivemongo.domain.Beer;
import dev.afcasco.reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);
}
