package dio.api_rest.controller;

import dio.api_rest.controller.dto.UserDto;
import dio.api_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/users")
@Tag(name = "Controlador do usuário", description = "https://github.com/RinoaYK/api-rest")
public record UserController(UserService userService) {

    @GetMapping
    @Operation(summary = "Get all users", description = "Retorna uma lista dos usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso!!")
    })
    public ResponseEntity<List<UserDto>> findAll() {
        var users = userService.findAll();
        var usersDto = users.stream().map(UserDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID", description = "Retorna um usuário relativo ao ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso!!"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado!")
    })
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Cria um novo usuário, retorna o usuário criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso!!"),
            @ApiResponse(responseCode = "422", description = "ID inválido!!")
    })
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        var user = userService.create(userDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(new UserDto(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Atualiza um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!!"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado!"),
            @ApiResponse(responseCode = "422", description = "ID inválido!!")
    })
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        var user = userService.update(id, userDto.toModel());
        return ResponseEntity.ok(new UserDto(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deleta um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso!!"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

