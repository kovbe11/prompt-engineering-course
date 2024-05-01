package com.example.experiment.service.exception;

public class CompilationFailedException extends Exception {

    public CompilationFailedException(String runId, String imageId) {
        super(String.format("Compilation failed for run with id: %s, and image id: %s", runId, imageId));
    }

}
