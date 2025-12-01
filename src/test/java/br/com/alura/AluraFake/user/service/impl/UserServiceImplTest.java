package br.com.alura.AluraFake.user.service.impl;

import br.com.alura.AluraFake.user.UserRepository;
import br.com.alura.AluraFake.user.dto.NewUserDTO;
import br.com.alura.AluraFake.user.entities.User;
import br.com.alura.AluraFake.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_should_throw_when_email_exists() {
        NewUserDTO dto = new NewUserDTO();
        dto.setEmail("duplicate@example.com");
        dto.setName("Name");
        dto.setRole(Role.STUDENT);

        when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já cadastrado no sistema");
    }

    @Test
    void createUser_should_save_when_email_not_exists() {
        NewUserDTO dto = new NewUserDTO();
        dto.setEmail("ok@example.com");
        dto.setName("Name");
        dto.setRole(Role.STUDENT);

        when(userRepository.existsByEmail("ok@example.com")).thenReturn(false);

        userService.createUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("ok@example.com");
        assertThat(savedUser.getName()).isEqualTo("Name");
        assertThat(savedUser.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void listUsers_should_map_entities_to_dto() {
        User user = new User("User 1", "user1@test.com", Role.STUDENT);
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertThat(userService.listUsers())
                .hasSize(1)
                .allSatisfy(item -> {
                    assertThat(item.getName()).isEqualTo("User 1");
                    assertThat(item.getEmail()).isEqualTo("user1@test.com");
                });
    }
}
