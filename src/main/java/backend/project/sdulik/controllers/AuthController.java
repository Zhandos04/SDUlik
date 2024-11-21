package backend.project.sdulik.controllers;

import backend.project.sdulik.config.CustomAuthenticationProvider;
import backend.project.sdulik.dto.requests.*;
import backend.project.sdulik.dto.responses.AuthDTO;
import backend.project.sdulik.entities.User;
import backend.project.sdulik.exceptions.UserAlreadyExistsException;
import backend.project.sdulik.jwt.JwtService;
import backend.project.sdulik.services.EmailService;
import backend.project.sdulik.services.TokenBlacklistService;
import backend.project.sdulik.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name="Auth", description="Аутентификация и авторизация")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final EmailService emailService;

    @PostMapping( "/signup")
    @Operation(summary = "Новый пользователь создать ету", description = "Астыдагы объект бойынша осы эндпоинтка пост запрос жибересин" +
            ". После этого жазган емайлды подверждать ету керек ол ушин /verify-email деген эндпоинтка емайлмен почтага келген кодты жибересин." +
            " Сосын барып пользователь успешно тиркеледи")
    @ApiResponse(responseCode = "202", description = "Code sent successfully!")
    @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content)
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        userService.registerNewUser(userDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Code sent successfully!");
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Верификация емайла", description = "Емайл мен кодты жибересин. Осыдан кейин смело логин " +
            "жасай бере аласын если кодты дурыс жиберсен")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Incorrect reset code")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> verifyEmail(@RequestBody CodeDTO codeDTO) {
        Optional<User> userOptional = userService.getUserByEmail(codeDTO.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();
        if (!user.getConfirmationCode().equals(codeDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Code");
        }

        user.setIsVerified(true);
        userService.update(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    @Operation(summary = "Логин киру сайтка", description = "Емайлмен парольди жазып пост запрос жибересин." +
            "В ответ токен аласын, Сол токенди озине сактап алп сайтка киргеннен кейинги барлык запроска колданасын. Чтобы " +
            "бэкенд соны расшифровать етип кай пользовательден келип жатканын билуим ушин")
    @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    @ApiResponse(responseCode = "401", description = "Incorrect ID or password")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userService.getUserByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } else if (!userOptional.get().getIsVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This user is not verified yet");
        }
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userOptional.get().getEmail(), loginDTO.getPassword()));
        AuthDTO authDTO = new AuthDTO();
        authDTO.setName(userOptional.get().getFirstName());
        authDTO.setToken(jwtService.generateToken(loginDTO.getEmail()));
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "Аккаунттан шыгу.", description = "logout баскан кезде осы эндпоинтка токен жибересин " +
            "Bearer кылып как обычно бэкенд ол токенди черный списокка тыгады. Сосын келеси логинда баска токен аласын")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Извлекаем дату истечения токена
            Date expirationTime = jwtService.extractExpiration(token);

            // Добавляем токен в черный список
            tokenBlacklistService.addTokenToBlacklist(token, expirationTime);

            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Пароль восстановить ету", description = "Емайлынды осы эндпоинтка жибересин. Бэк осы емайлга код жибереди восстановить ету ушин парольди")
    @ApiResponse(responseCode = "200", description = "Reset code sent successfully")
    @ApiResponse(responseCode = "404", description = "Email not found")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO) {
        Optional<User> user = userService.getUserByEmail(emailDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
        if(!user.get().getIsVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This user is not verified yet");
        }

        String code = generateCode();
        userService.saveUserConfirmationCode(user.get().getId(), code);

        emailService.sendEmail(emailDTO.getEmail(), "SDUlik Reset Password", "Your code is: " + code);

        return ResponseEntity.ok("Reset password instructions have been sent to your email.");
    }
    @PostMapping("/verify-code")
    @Operation(summary = "Кодты тексеру ", description = "Емайлмен кодты жибересин. Бэк кодты тексереди дурыс болса" +
            "200 успешно деген ответ барады сол кезде новый пароль жазатын пейджга ауыстырасын")
    @ApiResponse(responseCode = "200", description = "Reset code verified successfully")
    @ApiResponse(responseCode = "401", description = "Incorrect reset code")
    public ResponseEntity<?> verifyPassword(@RequestBody CodeDTO codeDTO) {
        Optional<User> user = userService.getUserByEmail(codeDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
        if(!user.get().getConfirmationCode().equals(codeDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Code");
        }
        return ResponseEntity.ok("Code is verified!");
    }

    @PostMapping("/update-password")
    @Operation(summary = "Новый пароль", description = "Емайлмен новый парольди жиберип парольди обновить етесин")
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        Optional<User> user = userService.getUserByEmail(updatePasswordDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
        user.get().setPassword(updatePasswordDTO.getPassword());
        userService.updatePassword(user.get());
        return ResponseEntity.ok("Password is updated!");
    }
    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }
}