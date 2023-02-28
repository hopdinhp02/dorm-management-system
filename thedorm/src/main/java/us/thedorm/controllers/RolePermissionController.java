package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Role;
import us.thedorm.models.RolePermission;
import us.thedorm.repositories.RolePermissionRepository;
import us.thedorm.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/role-permissions")
public class RolePermissionController {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllRolePermissions() {
        List<RolePermission> foundRolePermissions = rolePermissionRepository.findAll();
        if (foundRolePermissions.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundRolePermissions)
        );

    }

    @GetMapping("/{id}")
    Optional<RolePermission> findById(@PathVariable Long id) {

        return rolePermissionRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertRole(@RequestBody RolePermission newRolePermission) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", rolePermissionRepository.save(newRolePermission))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateRolePermission(@RequestBody RolePermission newRolePermission, @PathVariable Long id) {

        RolePermission updateRolePermission = rolePermissionRepository.findById(id)
                .map(rolePermission -> {
                    rolePermission.setRole(newRolePermission.getRole());
                    rolePermission.setPermission(newRolePermission.getPermission());

                    return rolePermissionRepository.save(rolePermission);
                }).orElseGet(() -> null);
        if(updateRolePermission != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateRolePermission)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteRolePermission(@PathVariable Long id) {

        boolean exists = rolePermissionRepository.existsById(id);
        if (exists) {
            rolePermissionRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
