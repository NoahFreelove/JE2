package org.JE.JE2.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Means return value can be null, so check that its valid!
@Retention(RetentionPolicy.CLASS)
public @interface Nullable {
}
