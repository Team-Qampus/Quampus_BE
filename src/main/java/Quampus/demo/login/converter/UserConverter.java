package Quampus.demo.login.converter;

import Quampus.demo.login.dto.UserRequestDTO;
import Quampus.demo.login.dto.UserResponseDTO;
import Quampus.demo.login.entity.User;

public class UserConverter {

    public static User toUser(UserRequestDTO.JoinDTO joinDTO) {
        return User.builder()
                .name(joinDTO.getName())
                .build();
    }

    public static UserResponseDTO.JoinResultDTO toJoinResultDTO(User user) {
        return UserResponseDTO.JoinResultDTO.builder()
                .userId(user.getId())
                .createAt(user.getCreated_date())
                .build();
    }




}
