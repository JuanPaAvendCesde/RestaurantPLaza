package org.pragma.restaurantplaza.infrastructure.input.rest;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.handler.OwnerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final OwnerHandler ownerHandler;

    @PostMapping("/saveOwner")
    public ResponseEntity<String>saveOwner (@Valid @RequestBody OwnerRequest ownerRequest) {
        ownerRequest.setRol("Owner");
        ownerHandler.saveOwner(ownerRequest);
        return ResponseEntity.ok("Owner created successfully");
    }
}
