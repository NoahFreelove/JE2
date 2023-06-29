package org.JE.JE2.Annotations;

/**
 * Marks a field which is public and can be mutated, but mutating will
 * not have any effect on the general class.
 * It is not advised to mutate a read only variable, but it is often quickly overwritten by a reliable value.
 */
public @interface ReadOnly {
}
