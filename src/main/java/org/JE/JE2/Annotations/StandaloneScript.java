package org.JE.JE2.Annotations;

/*
    Describes a script which is fully functional when added to an object or when standalone.
    In these kinds of scripts getAttachedObject() can never be used as that would make it
    reliant on being added to a GameObject.
*/
public @interface StandaloneScript {
}
