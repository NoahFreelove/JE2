package org.JE.JE2.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// To only be run from the GL thread.
@Retention(RetentionPolicy.CLASS)
public @interface GLThread {
}
