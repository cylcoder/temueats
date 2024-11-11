package com.sparta.temueats.store.util;

import com.sparta.temueats.global.ex.CustomApiException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidUtils {

    public static void throwIfHasErrors(BindingResult bindingResult, String errorMessagePrefix) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new CustomApiException(errorMessagePrefix + ": " + errorMessages);
        }
    }

}
