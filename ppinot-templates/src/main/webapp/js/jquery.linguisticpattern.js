(function($) {

$.fn.linguisticPattern = function(prefix, options, load, onChange) {
    return this.each(function() {
        var dom = $(this);
        var data = dom.data("value");       
        if (typeof load == "undefined" && typeof data != "undefined")
        	load = data;
        
        new LinguisticPattern(prefix, options, dom, onChange).recursiveDisplaySelect(load);
	});
	//if (loaded) {
		
	//}
	//$(elem).click(function() {
	//	recursiveDisplaySelect(prefix, options, elem);
	//});
};

/*
options: [{
	text: "The duration between time instants when <Event> and when <Event>",
	content: function(option) {
		return option.text;
	}
	...
	content: function(option, container) {
		str = "The duration between time instants when <div"
		recursiveDisplaySelect()
	}
},
...]
*/

function LinguisticPattern(prefix, options, elem, onChange) {
	this.prefix = prefix;
	this.options = options;
	this.elem = elem;
	this.onChange = onChange;

	$(elem).data("value", {});
};

$.extend(LinguisticPattern.prototype, {

	getCurrentValue: function() {		
		return $(this.elem).data("value").value;
	},

	getContainedValue: function() {
		return $(this.elem).data("value").contained;
	},

	setCurrentValue: function(value) {
		var storedValue = $(this.elem).data("value");
		storedValue.value = value;

		$(this.elem).data("value", storedValue);

		this.notifyChange(storedValue);
	},

	setContainedValue: function(value) {
		var storedValue = $(this.elem).data("value");
		storedValue.contained = value;

		$(this.elem).data("value", storedValue);

		this.notifyChange(storedValue);
	},

	setContainedValueByName: function(containedName, value) {
		var storedValue = $(this.elem).data("value");
		if (typeof storedValue.contained == "undefined") {
			storedValue.contained = {};
		}		
		storedValue.contained[containedName] = value;
		$(this.elem).data("containedValue", storedValue);

		this.notifyChange(storedValue);
	},

	notifyChange: function (value) {
		if (typeof this.onChange != "undefined")
			this.onChange(value);
	},

	recursiveDisplaySelect: function(load) {
		var elem = this.elem;
		var options = this.options;
		var prefix = this.prefix;

		if ($(elem).children('select').length == 0) {
			var currentid = this.getCurrentValue();
			if (typeof currentid == "undefined" && typeof load != "undefined") {
				currentid = load.value;
			}

			var that = this;
			var goBackFunction = function() {
				that.recursiveDisplaySelect();
			}

			$(elem).off("click").attr("tabindex", -1).empty();
			this.appendPrefix(goBackFunction);

	        var selectbox = this.createSelect(options, currentid);
			selectbox.appendTo(elem).focus();
			selectbox.blur(function() {
				var value = $(this).val();
				that.processSelection(value, goBackFunction, load);
			});		

			if (typeof load != "undefined")
				selectbox.blur();
		}
	},

	appendPrefix: function(goBackFunction) {
		var prefix = this.prefix;

		if (prefix != "") {
			$("<span tabindex='0'>"+prefix+" </span>").click(goBackFunction).focus(goBackFunction).appendTo(this.elem);
		}
	},

	createSelect: function(options, currentid) {
		var str = "";

		for(i=0; i< options.length; i++) {
			if (currentid == i) 
				str += "<option selected value='"+i+"'>" + options[i].text+"</option>";
			else
				str += "<option value='"+i+"'>"+ options[i].text+"</option>";
		}
		return $("<select>"+str+"</select>");
	}, 



	processSelection: function(value, goBackFunction, load) {
		var elem = this.elem;
		var prefix = this.prefix;
		var that = this;

		$(elem).empty();
		this.setCurrentValue(value);

		this.appendPrefix(goBackFunction);

		if (typeof this.options[value].appendContent != "undefined") {
			var loadInfo = load;
			if (typeof load != "undefined")
				loadInfo = load.contained;
			this.options[value].appendContent(elem, goBackFunction, loadInfo, function(value) {
				that.setContainedValue(value);
			});					
		}
		else if (typeof this.options[value].content != "undefined") {
			this.appendContent(this.options[value].content, goBackFunction, load);
		}
		else {
			$("<span></span>").append(this.options[value].text).click(goBackFunction).appendTo(elem);
		}
	},

	appendContent: function(content, goBackFunction, load) {
		var elem = this.elem;
		var that = this;

		var prefixElem = $("<span>"+content.text+"</span>");
		prefixElem.click(goBackFunction).appendTo(elem);

		for (var i = content.patterns.length - 1; i >= 0; i--) {
			var pattern = content.patterns[i];
			var container = $("<span tabindex='0'></span>");
			container.insertAfter(prefixElem);
			var loadInfo;
			if (typeof load == "undefined" && typeof pattern.name != "undefined") {
				loadInfo = undefined;
			} else {
				loadInfo = load.contained[pattern.name];
			}

			container.linguisticPattern(
				pattern.prefix, 
				pattern.options, 
				loadInfo, 
				function(name) {
					return function(newValue)  {
						that.setContainedValueByName(name, newValue);
					}
				}(pattern.name)
			);
		}	
	}

});

})(jQuery);