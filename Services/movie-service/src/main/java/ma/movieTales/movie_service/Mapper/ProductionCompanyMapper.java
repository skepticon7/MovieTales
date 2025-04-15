package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.DTO.ProductionCompanyDTO;
import ma.movieTales.movie_service.Entity.Actor;
import ma.movieTales.movie_service.Entity.ProductionCompany;

import java.util.ArrayList;

public class ProductionCompanyMapper {
    public static ProductionCompanyDTO toDto(ProductionCompany productionCompany){
        if(productionCompany == null) return null;
        return ProductionCompanyDTO.builder()
                .name(productionCompany.getName())
                //.image(productionCompany.getImage())
                .build();
    }

    public static ProductionCompany toEntity(ProductionCompanyDTO productionCompanyDTO){
        if(productionCompanyDTO == null) return null;
        return ProductionCompany.builder()
                .name(productionCompanyDTO.getName())
                .movies(new ArrayList<>())
                //.image(productionCompanyDTO.getImage())
                .build();
    }
}
