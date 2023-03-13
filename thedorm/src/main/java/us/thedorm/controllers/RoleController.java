package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Role;
import us.thedorm.models.UserRole;
import us.thedorm.repositories.RoleRepository;
import us.thedorm.repositories.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllRoles() {
        List<Role> foundRoles = roleRepository.findAll();
        if (foundRoles.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundRoles)
        );

    }

    @GetMapping("/{id}")
    Optional<Role> findById(@PathVariable Long id) {

        return roleRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertRole(@RequestBody Role newRole) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", roleRepository.save(newRole))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateRole(@RequestBody Role newRole, @PathVariable Long id) {

        Role updateRole = roleRepository.findById(id)
                .map(role -> {
                    role.setUserRoles(newRole.getUserRoles());
                    role.setRolePermissions(newRole.getRolePermissions());

                    return roleRepository.save(role);
                }).orElseGet(() -> null);
        if(updateRole != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateRole)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteRole(@PathVariable Long id) {

        boolean exists = roleRepository.existsById(id);
        if (exists) {
            roleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
