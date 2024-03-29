package us.thedorm.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


  private String username;
  private String password;
  private String name;
  private String email;

  private String phone;
  private boolean gender;
  private String role;

}
