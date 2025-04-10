package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.Entity.Actor;

public class ActorMapper {
    public static ActorDTO toDto(Actor actor){
        if(actor == null) return null;
        return ActorDTO.builder()
                .name(actor.getName())
                .build();
    }

    public static Actor toEntity(ActorDTO actorDTO){
        if(actorDTO == null) return null;
        return Actor.builder()
                .name(actorDTO.getName())
                .build();
    }
}
