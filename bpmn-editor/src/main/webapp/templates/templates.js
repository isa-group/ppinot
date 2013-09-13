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
    cleanScope(ppiSet);
}

function cleanScope(ppiSet) {
    iteratePPIs(ppiSet, function(ppi) {
        if (ppi.scope != null) {
            var scope = {kind: ppi.scope.kind};

            if (ppi.scope.kind == "LastInstancesFilter") {
                scope.numberOfInstances = ppi.scope.numberOfInstances;
            }
            else if (ppi.scope.kind == "SimpleTimeFilter") {
                scope.relative = ppi.scope.relative;
                scope.frequency = ppi.scope.frequency;
                scope.period = ppi.scope.period;
            }
            else {
                scope = null;
            }

            ppi.scope = scope;
        }
    });
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

    // Loads the current model
    $scope.load($location.hash());
}