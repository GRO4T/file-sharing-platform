package com.gro4t.flux.files.exception;

public class FluxFileNotUploadedException extends RuntimeException {
    public FluxFileNotUploadedException() {
        super("File not uploaded");
    }
}
