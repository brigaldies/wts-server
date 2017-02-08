package com.infiniteintelligence.wts.services.security

import com.infiniteintelligence.wts.domain.security.Role
import com.infiniteintelligence.wts.domain.security.User
import com.infiniteintelligence.wts.domain.security.UserRole
import com.infiniteintelligence.wts.support.DaoSupport
import grails.transaction.Transactional

@Transactional
class SecurityService extends DaoSupport {

    def springSecurityService

    /**
     * Saves a new User, or updates an existing user.
     * @param user The user to save or update.
     * @return The saved or updated user.
     */
    User save(User user, boolean flush = false) {
        saveObject(user, flush)
    }

    /**
     * For client code readability, this is an alias for the save method.
     * @param user The user to update.
     * @return The updated user.
     */
    User update(User user, boolean flush = false) {
        save(user, flush)
    }

    /**
     * Saves a new Role, or updates an existing Role.
     * @param role The role to save or update.
     * @return The saved or updated role.
     */
    Role save(Role role, boolean flush = false) {
        saveObject(role, flush)
    }

    /**
     * Saves a new user-to-role association, or updates an existing association.
     * @param userRole The association to save or update.
     * @return The saved or updated association.
     */
    UserRole save(UserRole userRole, boolean flush = false) {
        saveObject(userRole, flush)
    }

    /**
     * For client code readability, this is an alias for the save method.
     * @param userRole The user-to-role association to update.
     * @return The updated Role-to-user association.
     */
    UserRole update(UserRole userRole, boolean flush = false) {
        save(userRole, flush)
    }

    /**
     * Save a new User and its Roles.
     * @param user User to persist
     * @param roles User's roles
     * @param flush Hibernate flush indicator
     * @return The saved/persisted User
     * @throws ValidationException if the saving of a UserRole fails.
     */
    User saveWithRoles(User user, List<Role> roles, boolean flush = false) {
        if (saveObject(user, flush)) {
            roles.each { Role role ->
                UserRole userRole = new UserRole(
                        user: user,
                        role: role)
                saveObject(userRole, flush, true)
            }
        }

        // Returned the persisted user, or the user with validation errors
        user
    }

    /**
     * Delete a user-to-role association
     * @param userRole The association to delete
     */
    def delete(UserRole userRole) {
        if (userRole) {
            userRole.delete()
            log.debug "Deleted user-to-role: $userRole"
        } else {
            def errMsg = "delete(): 'userRole' arg is null."
            log.error errMsg
            throw new IllegalArgumentException(errMsg)
        }
    }

    /**
     * Helper method to return the currently authenticated user
     * @return The currently authenticated user
     */
    @Transactional(readOnly = true)
    User getAuthenticatedUser() {
        User user = User.get(springSecurityService?.principal?.id)
        if (!user) {
            log.warn "Authenticated user not found."
        } else {
            log.debug "user: ${user.username}"
        }
        user
    }
}