package com.example.restapilearning.annotations;

import javax.ws.rs.NameBinding;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
}


