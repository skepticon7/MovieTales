package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.Entity.Actor;

import java.util.ArrayList;

public class ActorMapper {
    public static ActorDTO toDto(Actor actor){
        if(actor == null) return null;
        return ActorDTO.builder()
                .name(actor.getName())
                //.image(actor.getImage())
                .build();
    }

    public static Actor toEntity(ActorDTO actorDTO){
        if(actorDTO == null) return null;
        return Actor.builder()
                .movies(new ArrayList<>())
                .name(actorDTO.getName())
                //.image(actorDTO.getImage())
                .build();
    }
}
