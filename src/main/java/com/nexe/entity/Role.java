package com.nexe.entity;

/**
 * ROLE ENUM
 *
 * PURPOSE:
 * - Defines user roles in the system
 * - Used for authorization and access control
 *
 * EXAMPLES:
 * - USER: Regular application user
 * - ADMIN: Administrator with elevated privileges
 */
public enum Role {

    /**
     * REGULAR USER
     */
    ROLE_USER,

    /**
     * ADMINISTRATOR
     */
    ROLE_ADMIN
}