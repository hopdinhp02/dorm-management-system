package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.BookingRequest;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Branch;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.AuthRepository;
import us.thedorm.repositories.BranchRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/branchs")
public class BranchController {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private AuthRepository authRepository;

    @GetMapping("")

    ResponseEntity<ResponseObject> getAllBranches() {
        List<Branch> foundBranches = branchRepository.findAll();
        if (foundBranches.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBranches)
        );

    }

    @GetMapping("/{id}")
    Optional<Branch> findById(@PathVariable Long id) {
//        Optional<Branch> foundBranch = branchRepository.findById(id);
        return branchRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertBranch(@RequestBody Branch newBranch) {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int numInsert = authRepository.getNumberOfPermission(user.getId(),"BRANCH","INSERT");

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert successfully", branchRepository.save(newBranch))
            );


    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateBranch(@RequestBody Branch newBranch, @PathVariable Long id) {

        Branch updateBranch = branchRepository.findById(id)
                .map(branch -> {
                    branch.setName(newBranch.getName());
                    branch.setAddress(newBranch.getAddress());
                    branch.setPhone(newBranch.getPhone());
//                    branch.setTypeId(newBranch.getTypeId());
//                    branch.setStatus(newBranch.getStatus());
                    return branchRepository.save(branch);
                }).orElseGet(() -> null);
        if(updateBranch != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateBranch)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteBranch(@PathVariable Long id) {

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
