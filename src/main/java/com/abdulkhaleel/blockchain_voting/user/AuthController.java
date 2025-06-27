package com.abdulkhaleel.blockchain_voting.user;

import com.abdulkhaleel.blockchain_voting.security.jwt.JwtUtils;
import com.abdulkhaleel.blockchain_voting.security.services.UserDetailsImpl;
import com.abdulkhaleel.blockchain_voting.user.dto.JwtResponse;
import com.abdulkhaleel.blockchain_voting.user.dto.LoginRequest;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import com.abdulkhaleel.blockchain_voting.user.dto.RegisterRequest;
import com.abdulkhaleel.blockchain_voting.user.model.ERole;
import com.abdulkhaleel.blockchain_voting.user.model.Role;
import com.abdulkhaleel.blockchain_voting.user.model.User;
import com.abdulkhaleel.blockchain_voting.user.repository.RoleRepository;
import com.abdulkhaleel.blockchain_voting.user.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Step 1: Authenticate. This part works.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Step 2: Get the UserDetails to find the user's ID.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // --- THE FINAL FIX: MANUAL ROLE FETCHING ---
        // Step 3: Use our new native query to get the roles. This CANNOT fail.
        Set<Role> userRoles = roleRepository.findRolesByUserId(userId);

        // Step 4: Manually convert the Set<Role> to a List<String>.
        List<String> roles = userRoles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        // --- DEBUGGING ---
        System.out.println("---- DEBUG: Manually Fetched Roles ----");
        System.out.println(roles.toString());
        // --- END DEBUGGING ---

        // Step 5: Build the response with the manually fetched roles.
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    // In AuthController.java

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role 'USER' is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'ADMIN' is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'USER' is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
