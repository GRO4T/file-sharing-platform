package com.gro4t.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(FluxApplication.class, args);
  }
}
