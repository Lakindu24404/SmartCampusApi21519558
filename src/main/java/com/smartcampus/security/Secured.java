// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.security;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured { // annotation to protect endpoints
    String[] value() default {};
}
