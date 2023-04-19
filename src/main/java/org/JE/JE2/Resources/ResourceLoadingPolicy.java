package org.JE.JE2.Resources;

public enum ResourceLoadingPolicy {
    // Will not check if the resource already exists in memory, this will always create a new resource
    DONT_CHECK_IF_EXISTS,
    REPLACE_IF_EXISTS,
    // Will check if the resource already exists in memory by name, second fastest way to check
    CHECK_BY_NAME,
    // Will check if the resource already exists in memory by id, this is the fastest way to check
    //CHECK_BY_ID,
    // Will check if the resource already exists in memory by byte data, this is the slowest way to check, but it is the most accurate
    CHECK_BY_BYTE_DATA
}
