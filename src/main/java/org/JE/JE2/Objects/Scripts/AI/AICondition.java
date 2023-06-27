package org.JE.JE2.Objects.Scripts.AI;

public interface AICondition {
    default boolean passed() {
        return false;
    }
    default boolean failed() {
        return false;
    }

    default void check(){}

    default void onInterrupt(){}
}
