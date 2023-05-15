package com.prasant.spring6restmvc.controllers;

import com.prasant.spring6restmvc.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
//@ControllerAdvice
public class ExceptionController {
    //@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException() {
        log.debug("BeerController.handleResourceNotFoundException() called.");
        return ResponseEntity.notFound().build();
    }
}
