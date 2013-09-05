angular.module('templatesApp', ['measuresModule']);

function iteratePPIs(ppiSet, iterationFunction) {
    $(ppiSet).each(function() {
        $(this.ppis).each(function() {
            iterationFunction(this);
        });
    });

}

function assign(ppiSet, source, origin, transform) {
    iteratePPIs(ppiSet, function (ppi) {
        ppi[source] = [];
        $(ppi[origin]).each(function() {
            ppi[source].push(transform(this));
        });
    });
}


function loadArrays(ppiSet) {
    var loadTransform = function(value) {
        return {elem:value};
    };

    assign(ppiSet, "ngGoals", "goals", loadTransform);
    assign(ppiSet, "ngInformed", "informed", loadTransform);

}

function saveArrays(ppiSet) {
    var saveTransform = function(value) {
        return value.elem;
    };

    assign(ppiSet, "goals", "ngGoals", saveTransform);
    assign(ppiSet, "informed", "ngInformed", saveTransform);
}

function TemplatesCtrl($scope, $location, $http) {

	$http.get("service/models/").success(function(data) {
		$scope.models = data;
	});


    $scope.load = function(modelId) {
        $scope.modelName = modelId;
        var url = "service/model/"+modelId;
        $scope.model = new BPMNModel(modelId, url);
        $scope.ppis = [];
        $scope.model.load().then(function () {
            $http.get($scope.model.url+"/ppis").success(function(data) {
                $scope.ppis = data;
                loadArrays($scope.ppis);
            });
        })
    };

    $scope.remove = function(ppi) {
    	var index = $scope.ppis.indexOf(ppi);
    	$scope.ppis.splice(index, 1);
    }

    $scope.save = function() {
        saveArrays($scope.ppis);
        $http.put($scope.model.url+"/ppis", $scope.ppis);
    }

    $scope.add = function() {
    	ppi = {
    		id: "00",
    		name: "PPI descriptive name",
    		description: "PPI description",
    		goals: "strategic or operational goals the PPI is related to",
    		measuredBy: {unit: "unit"},
    		target: "The PPI value must",
    		scope: "The process instances considered for this PPI are",
    		source: "source from which the PPI measure can be obtained",
    		responsible: "role | department | organization | person",
    		informed: "role | department | organization | person",
    		comments: "additional comments about the PPI"
    	};
    	$scope.ppis.push(ppi);
    }

    // Loads the current model
    $scope.load($location.hash());
}