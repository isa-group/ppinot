angular.module('navbarModule', ['loginModule', 'ui.bootstrap'])
  .directive('navbar', ['$rootScope', '$location', '$http', function($rootScope, $location, $http){
    return {
      restrict: 'E',
      replace: true,
      scope: {title: '@', navbar: '=data'},
      templateUrl: 'common/ng-navbar-template.html',
      controller: function ($scope) {
        $scope.navbar = {};

        // Loads the list of models
        $http.get("service/models/").success(function(data) {
            $scope.navbar.models = {};
            angular.forEach(data, function(info) {
                $scope.navbar.models[info.modelId] = info;
            });
            updateModel();
        });

        // Loads the current model
        $scope.$watch(function() {return $location.path();}, function(path) {
            var modelId = path.substr(1);
            $scope.navbar.currentModelId = modelId;
            updateModel();
        })

        function updateModel() {
            if ($scope.navbar.models) {
                $scope.navbar.currentModel = $scope.navbar.models[$scope.navbar.currentModelId];
            }
        }
      }
    }
  }]);