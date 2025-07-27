package com.gro4t.flux.files.exception;

public class FluxFileNotFoundException extends RuntimeException {
    public FluxFileNotFoundException() {
        super("File not found");
    }
}
