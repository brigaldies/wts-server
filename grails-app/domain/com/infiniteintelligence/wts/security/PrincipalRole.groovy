package com.infiniteintelligence.wts.security

import org.apache.commons.lang.builder.HashCodeBuilder

class PrincipalRole implements Serializable {

	private static final long serialVersionUID = 1

	Principal principal
	Role role

	boolean equals(other) {
		if (!(other instanceof PrincipalRole)) {
			return false
		}

		other.principal?.id == principal?.id &&
		other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (principal) builder.append(principal.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static PrincipalRole get(long principalId, long roleId) {
		PrincipalRole.where {
			principal == Principal.load(principalId) &&
			role == Role.load(roleId)
		}.get()
	}

	static boolean exists(long principalId, long roleId) {
		PrincipalRole.where {
			principal == Principal.load(principalId) &&
			role == Role.load(roleId)
		}.count() > 0
	}

	static PrincipalRole create(Principal principal, Role role, boolean flush = false) {
		def instance = new PrincipalRole(principal: principal, role: role)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(Principal u, Role r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = PrincipalRole.where {
			principal == Principal.load(u.id) &&
			role == Role.load(r.id)
		}.deleteAll()

		if (flush) { PrincipalRole.withSession { it.flush() } }

		rowCount > 0
	}

	static void removeAll(Principal u, boolean flush = false) {
		if (u == null) return

		PrincipalRole.where {
			principal == Principal.load(u.id)
		}.deleteAll()

		if (flush) { PrincipalRole.withSession { it.flush() } }
	}

	static void removeAll(Role r, boolean flush = false) {
		if (r == null) return

		PrincipalRole.where {
			role == Role.load(r.id)
		}.deleteAll()

		if (flush) { PrincipalRole.withSession { it.flush() } }
	}

	static constraints = {
		role validator: { Role r, PrincipalRole ur ->
			if (ur.principal == null) return
			boolean existing = false
			PrincipalRole.withNewSession {
				existing = PrincipalRole.exists(ur.principal.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['role', 'principal']
		version false
	}
}
