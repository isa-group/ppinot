angular.module('ralApp', ['navbarModule','loginModule','ui.bootstrap'])
.filter('array', function() {
  return function(items) {
    var filtered = [];
    angular.forEach(items, function(item) {
      filtered.push(item);
    });
   return filtered;
  };
});

function AssignmentCtrl($scope, $http, $log) {

    $scope.rasciRoles = {
        "responsible": "R",
        "accountable": "A",
        "support": "S",
        "consulted": "C",
        "informed": "I"
    };

    $scope.rasciCell = null;

    $scope.loadCell = function(data) {
        if (!data.assignment[data.activity])
            data.assignment[data.activity] = [];

        var cell = {assign: data.assignment[data.activity], role: data.role, activity: data.activity};

        angular.forEach($scope.rasciRoles, function(abbr, role) {
            cell[role] = {enabled: false, binding: ""};
        });

        angular.forEach(data.assignment[data.activity], function(boundRole) {
            if (boundRole.role == data.role) {
                cell[boundRole.type] = {enabled: true, binding: boundRole.bindingExpression};
            }
        });

        $scope.rasciCell = cell;
    }

    $scope.saveCell = function(cell) {
        var assign = cell.assign;

        var i = assign.length;
        while(i--) {
            if (assign[i].role == cell.role) {
               assign.splice(i,1);
            }
        }

        angular.forEach($scope.rasciRoles, function(abbr, rasciRole) {
            if (cell[rasciRole].enabled) {
                assign.push({type: rasciRole, role: cell.role, bindingExpression: cell[rasciRole].binding});
            }
        });

        $scope.rasciCell = null;
    }

    $scope.depict = function(data) {
        var result = [];
        angular.forEach(data.assignment[data.activity], function(boundRole) {
            if (boundRole.role == data.role) {
                result.push($scope.rasciRoles[boundRole.type]);
            }
        });

        return result.join(" / ");
    }

    $scope.depictDetails = function(data) {
        var result = [];
        angular.forEach(data.assignment[data.activity], function(boundRole) {
            if (boundRole.role == data.role) {
                result.push(boundRole.type + ": " + boundRole.bindingExpression);
            }
        });

        return result.join("<br/>");
    }

    $scope.load = function(currentModel) {
        $scope.bpmnModel = new BPMNModel(currentModel.modelId, currentModel.url);
        $scope.bpmnModel.load().then(function () {
            $scope.$apply('bpmnModel');

            $http.get(currentModel.url+"/json").success(function(data) {
                if (! data.extensions) {
                    data.extensions = {};
                }
                if (! data.extensions.assignments) {
                    data.extensions.assignments = {};
                }
                $scope.raw = data;
                $scope.assignments = data.extensions.assignments;

                angular.forEach($scope.bpmnModel.processes, function(p, id) {
                    if (! $scope.assignments[p.processName])
                        $scope.assignments[p.processName] = {ralAssignment: {}, rasciAssignment: {}};
                });
            });
        });
    };

    $scope.$watch("assignments.organizationalModel", function(modelId, oldModelId) {
        if (modelId) {
            $scope.loadOrganization(modelId);
        }
    });

    $scope.loadOrganization = function(modelId) {
        $http.get($scope.navbar.models[modelId].url+"/json").success(function(data) {
            $scope.organization = data.model;
        });
    }

    $scope.$watch("navbar.currentModel", function(currentModel, oldModel) {
        $log.info("Current model: " + currentModel);
        if (currentModel)
            $scope.load(currentModel);
    });


    $scope.save = function () {
        $log.info("Saving model...");
        $log.info($scope.raw);
        $http.put($scope.navbar.currentModel.url+"/json", $scope.raw);
    }

}
