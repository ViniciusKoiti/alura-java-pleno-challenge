package br.com.alura.AluraFake.user.service;

import br.com.alura.AluraFake.user.dto.NewUserDTO;
import br.com.alura.AluraFake.user.dto.UserListItemDTO;

import java.util.List;

public interface UserService {

    void createUser(NewUserDTO newUser);

    List<UserListItemDTO> listUsers();
}
