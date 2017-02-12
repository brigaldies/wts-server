package wts.server

import com.infiniteintelligence.wts.domain.organization.Address
import com.infiniteintelligence.wts.domain.organization.Asset
import com.infiniteintelligence.wts.domain.organization.AssetTypeCode
import com.infiniteintelligence.wts.domain.organization.Organization
import com.infiniteintelligence.wts.domain.security.Role
import com.infiniteintelligence.wts.domain.security.User
import com.infiniteintelligence.wts.domain.security.UserRole
import com.infiniteintelligence.wts.domain.threat.ThreatTypeCode
import com.infiniteintelligence.wts.services.crud.CrudService
import com.infiniteintelligence.wts.services.security.SecurityService
import grails.core.GrailsApplication
import groovy.util.logging.Slf4j

@Slf4j
class BootStrap {

    SecurityService securityService
    GrailsApplication grailsApplication
    CrudService crudService

    def init = { servletContext ->

        createRolesAndSystemAccount()
        createTestUsers()
        populateCodeTables()

        // Test creation of the codes
        List<ThreatTypeCode> threatTypes = ThreatTypeCode.list()
        assert threatTypes.size() > 0
        threatTypes.each { ThreatTypeCode threatTypeCode ->
            log.info "${threatTypeCode}"
        }

        // Create test data from external configuration file
        createTestData()
        // Test the creation of the test data
        List<Asset> assets = Asset.list()
        assert assets.size() > 0
        assets.each { Asset asset ->
            log.info "${asset}"
        }

    }
    def destroy = {
    }

/**
 * Create the Security ROLEs, as well as the SYSTEM account if it does not already exist.
 *
 * The SYSTEM account is used by the system only. The account is permanently locked, and cannot be logged in to.
 */
    def createRolesAndSystemAccount() {
        assert securityService != null

        // Create the roles if they don't exist.
        def role = 'ROLE_SYSTEM'
        log.debug "Checking on role ${role}..."
        def systemRole = Role.findByAuthority(role)
        if (!systemRole) {
            systemRole = securityService.save(new Role(authority: role))
            log.info "Created role $systemRole"
        } else {
            log.info "Already created: Role $systemRole"
        }

        role = 'ROLE_ADMIN'
        log.debug "Checking on role ${role}..."
        def adminRole = Role.findByAuthority(role)
        if (!adminRole) {
            adminRole = securityService.save(new Role(authority: role))
            log.info "Created role $adminRole"
        } else {
            log.info "Already created: Role $adminRole"
        }

        role = 'ROLE_USER'
        log.debug "Checking on role ${role}..."
        def userRole = Role.findByAuthority(role)
        if (!userRole) {
            userRole = securityService.save(new Role(authority: role))
            log.info "Created role $userRole"
        } else {
            log.info "Already created: Role $userRole"
        }

        // System user
        def username = 'system'
        log.debug "Checking on user ${username}..."
        def systemUser = User.findByUsername(username)
        if (!systemUser) {
            systemUser = new User(
                    username: username,
                    password: username, // The account is always locked, hence there is no login allowed.
                    firstName: 'The',
                    lastName: 'System',
                    email: 'system@infiniteintelligence.com',
                    accountLocked: true)
            if (!securityService.save(systemUser)) {
                throw new RuntimeException('Could not create the SYSTEM account!')
            } else {
                log.info "SYSTEM account created."
            }
            if (!securityService.save(new UserRole(
                    user: systemUser,
                    role: systemRole))) {
                throw new RuntimeException('Could not grant the ROLE_SYSTEM role to the SYSTEM account!')
            } else {
                log.info "SYSTEM account granted with the ROLE_SYSTEM role."
            }
        } else {
            log.info "SYSTEM account checked."
        }
    }

/**
 * Load test users
 */
    def createTestUsers() {

        log.info "---------- Create test users: BEGIN ----------"
        int roleCount = 0
        int userCount = 0

        assert securityService != null

        // Create the roles if they don't exist.
        def role = 'ROLE_ADMIN'
        def adminRole = Role.findByAuthority(role)
        if (!adminRole) {
            adminRole = securityService.save(new Role(authority: role))
            log.info "Created role $adminRole"
            roleCount++
        } else {
            log.info "Already created: Role $adminRole"
        }

        role = 'ROLE_USER'
        def userRole = Role.findByAuthority(role)
        if (!userRole) {
            userRole = securityService.save(new Role(authority: role))
            log.info "Created role $userRole"
            roleCount++
        } else {
            log.info "Already created: Role $userRole"
        }

        // Admin user
        def username = 'admin'
        log.debug "Checking on user ${username}..."
        def adminUser = User.findByUsername(username)
        if (!adminUser) {
            securityService.saveWithRoles(
                    new User(
                            username: username,
                            password: username,
                            firstName: 'The',
                            lastName: 'Administrator',
                            email: 'wts_admin@infiniteintelligence.com'),
                    [userRole, adminRole])
            log.info "User $username created."
            userCount++
        } else {
            log.info "Already created: User $username"
        }

        username = 'rigaldiesb'
        log.debug "Checking on user ${username}..."
        def userUser = User.findByUsername(username)
        if (!userUser) {
            securityService.saveWithRoles(
                    new User(
                            username: username,
                            password: username,
                            firstName: 'Bertrand',
                            lastName: 'Rigaldies',
                            email: 'brigaldies@infiniteintelligence.com'),
                    [userRole, adminRole])
            log.info "User $username created."
            userCount++
        } else {
            log.info "Already created: User $username"
        }

        log.info "---------- Create test roles ($roleCount) and users ($userCount): END ----------"
    }

    /**
     * Populate the code tables from the declared Code classes' inner enums.
     * @return true
     */
    def populateCodeTables() {
        List<Class> domainClasses = grailsApplication.getArtefacts("Domain")*.clazz
        domainClasses.each { Class domainClass ->
            // log.info "Domain class: ${domainClass}"
            if (domainClass.name.endsWith('Code')) {
                log.info "Domain code class: ${domainClass}"
                Class codeEnum = Class.forName(domainClass.name + '$eValue')
                log.info "eValue: ${codeEnum.getEnumConstants()}"
                codeEnum.getEnumConstants().each { e ->
                    String codeValue = e.name()
                    Object codeInstance = domainClass.newInstance([
                            code       : codeValue,
                            label      : codeValue,
                            description: codeValue,
                            enabled    : true
                    ])
                    crudService.create(codeInstance)
                }
            }
        }
        true
    }

    /**
     * Create an organization and asset as rudimentary test data.
     */
    def createTestData() {
        User systemUser = User.findByUsername('system')
        assert systemUser

        String createComment = 'Test data loaded by Bootstrap'

        // Company #1: ABC Inc.
        Organization organization = crudService.create(new Organization(
                name: 'ABC Inc.',
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        )) as Organization

        Address address = crudService.create(new Address(
                street1: '1 Main Street',
                city: 'Charlottesville',
                state: 'VA',
                country: 'USA',
                latitute: 38.029306,
                longitute: -78.476678,
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        ))

        Asset asset = crudService.create(new Asset(
                organization: organization,
                assetType: AssetTypeCode.get(AssetTypeCode.eValue.STORE.name()),
                address: address,
                temperatureThresholdLow: 30,
                temperatureThresholdHigh: 70,
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        ))

        // Company #2: DEF Inc.
        organization = crudService.create(new Organization(
                name: 'DEF Inc.',
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        )) as Organization

        address = crudService.create(new Address(
                street1: '1 Main Street',
                city: 'Richmond',
                state: 'VA',
                country: 'USA',
                latitute: 37.540725,
                longitute: -77.436048,
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        ))

        asset = crudService.create(new Asset(
                organization: organization,
                assetType: AssetTypeCode.get(AssetTypeCode.eValue.STORE.name()),
                address: address,
                temperatureThresholdLow: 40,
                temperatureThresholdHigh: 60,
                lastUpdatedByUser: systemUser,
                lastUpdatedComment: createComment,
        ))

    }
}