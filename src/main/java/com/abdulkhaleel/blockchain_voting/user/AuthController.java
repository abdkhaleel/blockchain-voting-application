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


    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // --- NEW, MORE ROBUST LOGIC ---

        // Step 1: Create the User object and set its direct properties.
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        // isEnabled is already true by default in the entity, which is good.

        // Step 2: Find the Role. This is a critical step.
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Default user role (ROLE_USER) not found in database."));

        // Step 3: Create the roles set and add the found role.
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        // Step 4: Set the roles collection on the user object.
        user.setRoles(roles);

        // --- DEBUGGING: Let's see the object RIGHT BEFORE we save it ---
        System.out.println("---- DEBUG: Saving User Object ----");
        System.out.println(user.toString());
        // --- END DEBUGGING ---

        // Step 5: Save the fully constructed user object to the database.
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
