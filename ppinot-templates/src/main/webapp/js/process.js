function Process(processName) {
	this.processName = processName;
	this.processNames = [processName];
	this.processStates = ["Start", "Cancel","End"];
	this.activityNames = [];
	this.activityNameId = {};
	this.activityIdName = {};
	this.activityStates = ["Start", "Cancel","End"];
	this.eventNames = [];
	this.dataObjectNames= [];
	this.dataObjectPropertyNames = [];
	this.dataObjectState= [];
}

jQuery.extend(Process.prototype, {
	load: function() {
		return jQuery.when(this.loadActivities(),this.loadEvents(),this.loadDataObjects());
	},

	loadActivities: function() {
		var that = this;
		return $.ajax({
			type: "GET",
			url: "/api/repository/processes/" + that.processName + "/activities",
			dataType: "json",
			success: function(data) {
				$(data).each(function (index) {
					if (this.name == "") {
						this.name = this.id;
					}
					that.activityNames.push(this.name);
					that.activityIdName[this.id] = this.name;
					that.activityNameId[this.name] = this.id;				
				});
			}
		});
	},

	loadEvents: function() {
		var that = this;
		return $.ajax({
			type: "GET",
			url: "/api/repository/processes/" + that.processName + "/events",
			dataType: "json",
			success: function(data) {
				$(data).each(function (index) {
					if (this.name == "") {
						this.name = this.id;
					}
					that.eventNames.push(this.name);
				});
			}
		});
	},

	loadDataObjects: function() {
		var that = this;
		return $.ajax({
			type: "GET",
			url: "/api/repository/processes/" + that.processName + "/dataobjects",
			dataType: "json",
			success: function(data) {
				$(data).each(function (index) {
					if (this.name == "") {
						this.name = this.id;
					}
					that.dataObjectNames.push(this.name);
				});
			}
		});
	},

	isActivity: function(id) {
		var result = false;

		if (typeof this.activityIdName[id] != "undefined")
			result = true;

		return result;
	}
});
