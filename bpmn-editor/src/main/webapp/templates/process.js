function BPMNModel(modelName, modelUrl) {
    this.modelName = modelName;
    this.url = modelUrl;
    this.processes = {};
}

jQuery.extend(BPMNModel.prototype, {
	load: function() {
		var that = this;
		return $.ajax({
			type: "GET",
			url: that.url + "/info",
			dataType: "json",
			success: function(data) {
			    $(data).each(function() {
			        var process = new Process(that.modelName, that.url);
			        process.load(this);
                    that.processes[this.id] = process;
			    });
			}
		});
	}
});

function Process(modelId, modelUrl) {
    this.modelId = modelId;
    this.modelUrl = modelUrl;
    this.processId = "";
	this.processName = "";
	this.processNames = [];
	this.processStates = ["Start", "Cancel","End"];
	this.activityNames = [];
	this.activityNameId = {};
	this.activityIdName = {};
	this.activityStates = ["Start", "Cancel","End"];
	this.eventNames = [];
	this.dataObjectNames= [];
	this.dataObjectPropertyNames = [];
	this.dataObjectState= [];

	this.id = {};
}

jQuery.extend(Process.prototype, {
	load: function(data) {
	    this.processId = data.id;
	    this.processName = data.name;
	    this.processNames = [data.name];
        this.loadActivities(data.activities);
        this.loadEvents(data.events);
        this.loadDataObjects(data.dataObjects);
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
