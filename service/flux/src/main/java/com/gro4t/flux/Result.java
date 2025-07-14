package com.gro4t.flux;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class Result<T> {
    public static <T> Result<T> withSuccess() {
        return new Result<>(true, Optional.empty(), Optional.empty());
    }

    public static <T> Result<T> withSuccessAndData(T data) {
        return new Result<>(true, Optional.empty(), Optional.of(data));
    }

    public static <T> Result<T> withError(String errorMessage) {
        return new Result<>(false, Optional.of(errorMessage), Optional.empty());
    }

    private final boolean success;
    private final Optional<String> errorMessage;
    private final Optional<T> data;
}
