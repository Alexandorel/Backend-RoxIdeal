package com.roxideal.backend.finish;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finishes")
public class FinishController {

    private final FinishService service;

    public FinishController(FinishService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinishDto> findAll() {
        return service.findAll();
    }
}
