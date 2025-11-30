package br.com.alura.AluraFake.user;

import br.com.alura.AluraFake.user.dto.NewUserDTO;
import br.com.alura.AluraFake.user.dto.UserListItemDTO;
import br.com.alura.AluraFake.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/user/new")
    public ResponseEntity<Void> newStudent(@RequestBody @Valid NewUserDTO newUser) {
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public List<UserListItemDTO> listAllUsers() {
        return userService.listUsers();
    }
}
