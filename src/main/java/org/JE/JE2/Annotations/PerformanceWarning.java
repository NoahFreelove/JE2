package org.JE.JE2.Annotations;

public @interface PerformanceWarning {
    int Severity() default 0;
    String Reason() default "Not Optimized";
}
