package us.thedorm.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import us.thedorm.config.JwtService;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.UserInfoRepository;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
  private final UserInfoRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    UserInfo.Role userRole = UserInfo.Role.USER;
    if(request.getRole() != null && request.getRole().equalsIgnoreCase("staff")){
      userRole =UserInfo.Role.STAFF;
    }else if (request.getRole() != null && request.getRole().equalsIgnoreCase("guard")){
      userRole = UserInfo.Role.GUARD;
    }else if(request.getRole() != null && request.getRole().equalsIgnoreCase("admin")){
      userRole = UserInfo.Role.ADMIN;
    }
    var user = UserInfo.builder()
            .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
        .role(userRole)
            .email(request.getEmail())
            .phone(request.getPhone())
            .gender(request.isGender())
            .isActive(true)
        .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = repository.findByUsername(request.getUsername())
        .orElseThrow();
    if(user.isActive()) {
      var jwtToken = jwtService.generateToken(user);
      return AuthenticationResponse.builder()
              .token(jwtToken)
              .build();
    }
    throw new DisabledException("Account is not active");

  }
}
