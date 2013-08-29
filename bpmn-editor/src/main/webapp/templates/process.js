function Process(processName, processUrl) {
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
	this.url = processUrl;

	this.id = {};
}

jQuery.extend(Process.prototype, {
	load: function() {
		var that = this;
		return $.ajax({
			type: "GET",
			url: that.url + "/info",
			dataType: "json",
			success: function(data) {
			    that.loadActivities(data.activities);
			    that.loadEvents(data.events);
			    that.loadDataObjects(data.dataObjects);
			}
		});
	},

	loadActivities: function(data) {
	    var that = this;
        $(data).each(function (index) {
            if (this.name == "") {
                this.name = this.id;
            }
            that.activityNames.push(this.name);
            that.activityIdName[this.id] = this.name;
            that.activityNameId[this.name] = this.id;
            that.id[this.id] = {name: this.name, type: "activity"};
        });
	},

	loadEvents: function(data) {
		var that = this;
        $(data).each(function (index) {
            if (this.name == "") {
                this.name = this.id;
            }
            that.eventNames.push(this.name);
            that.id[this.id] = {name: this.name, type: "event"};
        });
	},

	loadDataObjects: function(data) {
		var that = this;
        $(data).each(function (index) {
            if (this.name == "") {
                this.name = this.id;
            }
            that.dataObjectNames.push(this.name);
            that.id[this.id] = {name: this.name, type: "data object"};
        });
	},

	isActivity: function(id) {
		var result = false;

		if (typeof this.activityIdName[id] != "undefined")
			result = true;

		return result;
	}
});
