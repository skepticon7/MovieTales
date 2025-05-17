package ma.movieTales.movie_service.Mapper;

import ma.movieTales.movie_service.DTO.UserDTO;
import ma.movieTales.movie_service.Entity.User;

public class UserMapper {
    public static UserDTO toDto(User user){
        if(user == null) return null;
        return UserDTO.builder()
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                ._id(user.getUserId())
                .build();
    }

    public static User toEntity(UserDTO userDTO){
        if(userDTO == null) return null;
        return User.builder()
                .userId(userDTO.get_id())
                .avatarUrl(userDTO.getAvatarUrl())
                .username(userDTO.getUsername())
                .build();
    }
}
