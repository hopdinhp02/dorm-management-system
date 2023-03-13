package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Permission;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.RolePermission;
import us.thedorm.repositories.PermissionRepository;
import us.thedorm.repositories.RolePermissionRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/permissions")
public class PermissionController {
    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllPermissions() {
        List<Permission> foundPermissions = permissionRepository.findAll();
        if (foundPermissions.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundPermissions)
        );

    }

    @GetMapping("/{id}")
    Optional<Permission> findById(@PathVariable Long id) {

        return permissionRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertRole(@RequestBody Permission newPermission) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", permissionRepository.save(newPermission))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updatePermission(@RequestBody Permission newPermission, @PathVariable Long id) {

        Permission updatePermission = permissionRepository.findById(id)
                .map(permission -> {
                    permission.setRolePermissions(newPermission.getRolePermissions());

                    return permissionRepository.save(permission);
                }).orElseGet(() -> null);
        if(updatePermission != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updatePermission)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deletePermission(@PathVariable Long id) {

        boolean exists = permissionRepository.existsById(id);
        if (exists) {
            permissionRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

}
