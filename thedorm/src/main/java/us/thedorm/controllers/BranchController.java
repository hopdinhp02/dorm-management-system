package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.branch;
import us.thedorm.repositories.BranchRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/branchs")
public class BranchController {
    @Autowired
    private BranchRepository branchRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllProducts() {
        List<branch> foundProducts = branchRepository.findAll();
        if (foundProducts.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundProducts)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<branch> foundBranch = branchRepository.findById(id);
        return foundBranch.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBranch)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody branch newBranch) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", branchRepository.save(newBranch))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody branch newBranch, @PathVariable Long id) {
        branch updateBranch = branchRepository.findById(id)
                .map(branch -> {
                    branch.setName(newBranch.getName());
                    branch.setAddress(newBranch.getAddress());
                    branch.setPhone(newBranch.getPhone());
                    branch.setType_id(newBranch.getType_id());
                    branch.setStatus(newBranch.getStatus());
                    return branchRepository.save(branch);
                }).orElseGet(() -> branchRepository.save(newBranch));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert Product successfully", updateBranch)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = branchRepository.existsById(id);
        if (exists) {
            branchRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

}

