package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Request;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Role;
import us.thedorm.repositories.RequestRepository;
import us.thedorm.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/requests")
public class RequestController {
    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllRequests() {
        List<Request> foundRequests = requestRepository.findAll();
        if (foundRequests.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundRequests)
        );

    }

    @GetMapping("/{id}")
    Optional<Request> findById(@PathVariable Long id) {

        return requestRepository.findById(id);
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertRequest(@RequestBody Request newRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", requestRepository.save(newRequest))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateRequest(@RequestBody Request newRequest, @PathVariable Long id) {

        Request updateRequest = requestRepository.findById(id)
                .map(request -> {
                    request.setTitle(newRequest.getTitle());
                   request.setStatus(newRequest.getStatus());
                   request.setContent(newRequest.getContent());
                   request.setType(newRequest.getType());
                   request.setCreatedDate(newRequest.getCreatedDate());

                    return requestRepository.save(request);
                }).orElseGet(() -> null);
        if(updateRequest != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateRequest)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteRequest(@PathVariable Long id) {

        boolean exists = requestRepository.existsById(id);
        if (exists) {
            requestRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
