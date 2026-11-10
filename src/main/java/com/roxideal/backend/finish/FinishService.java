package com.roxideal.backend.finish;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinishService {

    private final FinishRepository repository;

    public FinishService(FinishRepository repository) {
        this.repository = repository;
    }

    public List<FinishDto> findAll() {
        return repository.findAll().stream().map(FinishDto::from).toList();
    }
}
