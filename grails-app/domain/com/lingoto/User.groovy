package com.lingoto

class User {

	transient springSecurityService
	
	Account account
	static hasMany = [lessons: Lesson]

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
		//Make sure constraints also match what's in UserRegistrationCommand in the UserController
		username blank:false, maxSize:100, unique:true, email:true
		//More constraints on password set in userControlle UserRegistrationCommand
		//setting a max size too low on password causes issues. Possibly due to encryption adding to the size of the password.
		password blank: false, minSize:6, maxSize:190, validator: { passwd, user -> passwd != user.username}
		account nullable: true
	}

	static mapping = {
		table "users"
		password column: '`password`'
		account lazy: false
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
	
	void setUsername(String username) {
		this.username = username?.trim()
	}
}
