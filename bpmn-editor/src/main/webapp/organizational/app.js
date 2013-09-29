angular.module('organizationalApp', ['navbarModule','loginModule']);

function OrganizationalCtrl($scope, $location, $http) {

    $scope.load = function(model) {
        $http.get(model.url+"/json").success(function(data) {
            $scope.data = data;
        });
    };

    $scope.$watch("navbar.currentModel", function(newVal, oldVal) {
        console.log(newVal);
        if (newVal)
            $scope.load(newVal);
    })
}