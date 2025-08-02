package com.gro4t.flux.files.exception;

public class FluxFileAlreadyExistsException extends RuntimeException {
  public FluxFileAlreadyExistsException() {
    super("File already exists");
  }
}
