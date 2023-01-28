package JE.Annotations;

public @interface PerformanceWarning {
    int Severity() default 0;
    String Reason() default "Not Optimized";
}
