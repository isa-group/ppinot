(function($) {

$.fn.linguisticPattern = function(prefix, options, load, onChange) {
    return this.each(function() {
        var dom = $(this);
        var data = dom.data("value");
        if (typeof prefix == "undefined")
        	prefix = "";
        if (typeof load == "undefined" && typeof data != "undefined")
        	load = data;
        

        new LinguisticPattern(prefix, convertOptions(options), dom, onChange).recursiveDisplaySelect(load);
	});
	//if (loaded) {
		
	//}
	//$(elem).click(function() {
	//	recursiveDisplaySelect(prefix, options, elem);
	//});
};

function convertOptions(options) {
	var result = [];
	for ( var i = 0; i < options.length; i++ ) {
		if (typeof options[i] == "string")
			result[i] = {text: options[i]};
		else if (typeof options[i].appendContent == "undefined")
			result[i] = convertOption(options[i]);
		else
			result[i] = options[i];
	}
	return result;
};

function convertOption(option) {
	var splits = option.text.split(/{|(?:}\s*{*)/); //}
	if (splits.length < 2) {
		return {text: option.text};
	}

	var result = {
		text: splits[0],
		content: {
			text: splits[0],
			patterns: []
		}
	};

	for (var i = 1; i < splits.length; i++) {
		if (typeof option[splits[i]] != "undefined") {
			var pattern = {
				name: splits[i],
				options: option[splits[i]].options
			};

			if (typeof option[splits[i]].prefix != "undefined") {
				result.text += option[splits[i]].prefix + " ";
				pattern.prefix = option[splits[i]].prefix;
			}

			result.text += "{" + splits[i] + "}";
			result.content.patterns.push(pattern);			
		}
	}

	return result;
};

/*
Internal format:

var measure = [
	{
		text: "The duration between the time instants when {event} and when {event}",
		content: {
			text: "the duration between time instants ",
			patterns: [
				{
					name: "start",
					prefix: "when",
					options: eventOptions
				},
				{
					name: "end",
					prefix: " and when",
					options: eventOptions
				}
			] 
		}
	},
	{
		text: "The number of times Event"
	}
];
*/

function LinguisticPattern(prefix, options, elem, onChange) {
	this.prefix = prefix;
	this.options = options;
	this.elem = elem;
	this.onChange = onChange;
	this.previousState = "";

	$(elem).data("value", {});
};

$.extend(LinguisticPattern.prototype, {

	getValue: function() {
		return $(this.elem).data("value");

	},

	getCurrentValue: function() {		
		return this.getValue().value;
	},

	getContainedValue: function() {
		return this.getValue().contained;
	},

	setCurrentValue: function(value) {
		var storedValue = this.getValue();
		storedValue.value = value;

		$(this.elem).data("value", storedValue);

		this.notifyChange(storedValue);
	},

	setContainedValue: function(value) {
		var storedValue = this.getValue();
		storedValue.contained = value;

		$(this.elem).data("value", storedValue);

		this.notifyChange(storedValue);
	},

	setContainedValueByName: function(containedName, value) {
		var storedValue = this.getValue();
		if (typeof storedValue.contained == "undefined") {
			storedValue.contained = {};
		}		
		storedValue.contained[containedName] = value;
		$(this.elem).data("value", storedValue);

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

			this.previousState = $(elem).html();
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

		if (this.getCurrentValue() == value) {
			load = this.getValue();
		} else {
			this.setCurrentValue(value);
		}

		$(elem).empty();

		this.appendPrefix(goBackFunction);

		if (typeof this.options[value].appendContent != "undefined") {
			var loadInfo = (typeof load != "undefined") ? load.contained : undefined;
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