package br.com.alura.AluraFake.user.service.impl;

import br.com.alura.AluraFake.user.UserRepository;
import br.com.alura.AluraFake.user.dto.NewUserDTO;
import br.com.alura.AluraFake.user.dto.UserListItemDTO;
import br.com.alura.AluraFake.user.entities.User;
import br.com.alura.AluraFake.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createUser(NewUserDTO newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }
        User user = newUser.toModel();
        userRepository.save(user);
    }

    @Override
    public List<UserListItemDTO> listUsers() {
        return userRepository.findAll().stream()
                .map(UserListItemDTO::new)
                .toList();
    }
}
