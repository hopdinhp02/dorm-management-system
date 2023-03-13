package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Branch;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.UserRole;
import us.thedorm.repositories.BranchRepository;
import us.thedorm.repositories.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/user-roles")
public class UserRoleController {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUserRoles() {
        List<UserRole> foundUserRoles = userRoleRepository.findAll();
        if (foundUserRoles.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundUserRoles)
        );

    }

    @GetMapping("/{id}")
    Optional<UserRole> findById(@PathVariable Long id) {

        return userRoleRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertUserRole(@RequestBody UserRole newUserRole) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", userRoleRepository.save(newUserRole))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateUserRole(@RequestBody UserRole newUserRole, @PathVariable Long id) {

        UserRole updateUserRole = userRoleRepository.findById(id)
                .map(userRole -> {
                    userRole.setUser_info(newUserRole.getUser_info());
                    userRole.setRole(newUserRole.getRole());

                    return userRoleRepository.save(userRole);
                }).orElseGet(() -> null);
        if(updateUserRole != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateUserRole)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteUserRole(@PathVariable Long id) {

        boolean exists = userRoleRepository.existsById(id);
        if (exists) {
            userRoleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
