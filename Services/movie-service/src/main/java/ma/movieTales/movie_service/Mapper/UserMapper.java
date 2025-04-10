package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.ActorDTO;
import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.Entity.Actor;
import ma.movieTales.movie_service.Entity.User;

public class UserMapper {
    public static UserDTO toDto(User user){
        if(user == null) return null;
        return UserDTO.builder()
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .userId(user.getUserId())
                .build();
    }

    public static Actor toEntity(ActorDTO actorDTO){
        if(actorDTO == null) return null;
        return Actor.builder()
                .name(actorDTO.getName())
                .build();
    }
}
