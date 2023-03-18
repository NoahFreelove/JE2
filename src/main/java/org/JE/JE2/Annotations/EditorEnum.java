package org.JE.JE2.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorEnum {
    String[] values() default {
        "None"
    };
}
