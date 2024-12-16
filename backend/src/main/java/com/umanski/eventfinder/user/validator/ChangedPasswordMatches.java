package com.umanski.eventfinder.user.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChangedPasswordMatchesValidator.class)
public @interface ChangedPasswordMatches {
    String message() default "New password and confirmation password do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
