package lingoto

import com.lingoto.ErrorComparator

class ErrorsTagLib {
	
	static namespace = "errors"
	
	def errorList = {attrs ->
		def output
		if(attrs.bean?.hasErrors()){
			StringBuilder sb = new StringBuilder()
			sb << """<div class="errors"><ul>"""
			def savedErrors = attrs.bean.errors.allErrors.findAll{ true }
			savedErrors = savedErrors?.unique(new ErrorComparator())
			savedErrors.each {
				sb << """<li>${g.message(["error":it])}</li>"""
			}
			sb << "</ul></div>"
			output = sb.toString()
		}
		
		out << output
	}
	
}
