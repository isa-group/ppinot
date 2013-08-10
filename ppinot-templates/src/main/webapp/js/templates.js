function TemplatesCtrl($scope, $http) {
	var processName = "base";

    $scope.process = new Process(processName);
    $scope.process.load().then(function () {
  	    $http.get("/api/repository/processes/"+processName+"/ppis").success(function(data) {
	        $scope.ppis = data;
    	});
    });

    $scope.remove = function(ppi) {
    	var index = $scope.ppis.indexOf(ppi);
    	$scope.ppis.splice(index, 1);
    }

    $scope.save = function() {
    	// For testing purposes only
    	$scope.ppis[1].measuredBy.baseMeasure = $scope.ppis[0].measuredBy;
    	$scope.ppis[1].measuredBy.aggregationFunction = "min";
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

}