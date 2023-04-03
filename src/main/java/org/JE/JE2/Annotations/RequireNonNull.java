package org.JE.JE2.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// error will be thrown if null value is passed through
@Retention(RetentionPolicy.CLASS)
public @interface RequireNonNull {
}
