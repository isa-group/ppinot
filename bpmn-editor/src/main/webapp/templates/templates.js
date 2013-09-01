angular.module('templatesApp', ['measuresModule']);


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
            });
        })
    };

    $scope.remove = function(ppi) {
    	var index = $scope.ppis.indexOf(ppi);
    	$scope.ppis.splice(index, 1);
    }

    $scope.save = function() {
    	// For testing purposes only
    	// $scope.ppis[1].measuredBy.baseMeasure = $scope.ppis[0].measuredBy;
    	// $scope.ppis[1].measuredBy.aggregationFunction = "min";
    	$scope.ppis[1].target.refMax=7;
    	if ($scope.ppis[1].target.refMin==5)
    		$scope.ppis[1].target.refMax = null;
    	$scope.ppis[1].target.refMin=5;
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