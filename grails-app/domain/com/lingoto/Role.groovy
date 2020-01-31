package com.lingoto

class Role {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true, maxSize:50
	}
}
