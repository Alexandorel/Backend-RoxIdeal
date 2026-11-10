package com.roxideal.backend.finish;

public class FinishNotFoundException extends RuntimeException {

    public FinishNotFoundException(Long id) {
        super("Finish not found: " + id);
    }
}
