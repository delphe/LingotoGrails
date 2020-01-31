package lingoto

class MessageTagLib {
	
	static namespace = "msg"
	
	def categoryDropDown = {attrs ->
		def msgBeginner = g.message(code: "category.beginner.label", default: "Beginner")
		def msgIntermediate = g.message(code: "category.intermediate.label", default: "Intermediate")
		def msgAdvanced = g.message(code: "category.advanced.label", default: "Advanced")
		def categoryList = [msgBeginner,msgIntermediate,msgAdvanced]
		
		out << g.select(["name":attrs.name, "class":attrs.class, "value":attrs.value, "from":categoryList, 
			"keys":['Beginner', 'Intermediate', 'Advanced'], "noSelection":attrs.noSelection,
			"onchange":attrs.onchange])
	}
	
}
