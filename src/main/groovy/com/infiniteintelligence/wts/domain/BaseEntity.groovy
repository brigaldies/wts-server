package com.infiniteintelligence.wts.domain

import com.infiniteintelligence.wts.domain.security.User

/**
 * Abstract base class to share fields amongst domain classes.
 *
 * Constraints are not specified here as constraints between base classes and extending concrete classes are not
 * merged.
 */
abstract class BaseEntity {

    Date dateCreated // Date the entity was created (Managed by GORM)
    Date lastUpdated // Date the entity was last updated (Managed by GORM)
    User lastUpdatedByUser // The user who created or last updated the entity (Managed by the application)
    String lastUpdatedComment
// Comment entered by the user who created or last updated the entity (Managed by the application)
}