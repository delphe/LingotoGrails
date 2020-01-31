package com.lingoto

import java.util.Comparator

class ErrorComparator implements Comparator {
	//Used to gather a unique list of errors to prevent duplicate messages.
	int compare(Object o1, Object o2) {
		def error1 = o1
		def error2 = o2
		if (error1.code != error2.code){
			return error1.code.compareTo(error2.code)
		}else{
			return error1.code.compareTo(error2.code)
		}
	}

	boolean equals(Object obj) {
		return this.equals(obj)
	}
}
