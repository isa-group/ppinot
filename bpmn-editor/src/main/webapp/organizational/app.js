angular.module('organizationalApp', ['navbarModule','loginModule','listHandling','ui.bootstrap','angular_taglist_directive'])
    .directive('positionslist', function(){
      return {
        restrict: 'A',
        replace: true,
        transclude: true,
        scope: { model: '=', positions: '=', placeholder: '@', add: '@', detailsCollapsed: '@'},
        template: '<ul class="positions" ng-transclude>' +
                    '<li class="liposition" ng-repeat="el in positions">' +
                       '<form style="display:inline-block" class="form-inline"><input type="text" ng-model="el.name" placeholder="{{placeholder}}" /></form>'+
                       '<ul class="actions inline">'+
                           '<li tooltip="Make position report to {{positions[$index-1].name}}" ng-hide="$index==0"><a class="btn-mini" ng-click="moveRight(positions, $index)"><i class="icon-chevron-right"></i></a></li>' +
                           '<li tooltip="Remove position"><a remove from="positions" index="{{$index}}"></a></li>'+
                           '<li><a class="btn-mini" ng-click="addCollapsed = !addCollapsed"><i class="icon-plus"></i></a></li>'+
                           '<li ng-hide="addCollapsed"><form addform style="margin-bottom: 0px" list="el.reportedBy" placeholder="Add position that reports to {{el.name}}"></form></li>'+
                           '<li ng-show="detailsCollapsed"><a class="btn-mini" ng-click="detailsCollapsed = false">Show people and roles</a></li>'+
                           '<li ng-hide="detailsCollapsed"><a class="btn-mini" ng-click="detailsCollapsed = true">Hide people and roles</a></li>'+
                       '</ul>'+
                       '<div ng-hide="detailsCollapsed" class="offset1">Occupied by the following people: <taglist tag-data="el.occupiedBy"><input ng-model="selPeople" typeahead="p.name for p in model.persons | filter:$viewValue | limitTo:8"/></taglist></div>'+
                       '<div ng-hide="detailsCollapsed" class="offset1">Plays the following roles: <taglist tag-data="el.roles"><input ng-model="selRoles" typeahead="role.name for role in model.roles | filter:$viewValue | limitTo:8"/></taglist></div>' +
                       '<subpositions model="model" positions="el.reportedBy" details-collapsed="{{detailsCollapsed}}" placeholder="{{placeholder}}" add="Add position that reports to {{el.name}}"/> ' +
                    '</li>' +
                    '</ul>',

        link: function($scope, $element) {
            $scope.addCollapsed = true;

            $scope.$watch("detailsCollapsed", function(newVal, oldVal) {
                var elem = $scope.$$childHead;
                while (elem) {
                    elem.detailsCollapsed = newVal;
                    elem = elem.$$nextSibling;
                }
            });

            if (! $scope.positions)
                $scope.positions = [];

            $scope.addElem = function(elems) {
                elems.push({name:""});
            };

            $scope.moveRight = function(elems, index) {
                var previous = elems[index-1];
                if (previous) {
                    if (! previous.reportedBy)
                        previous.reportedBy = [];

                    previous.reportedBy.push(elems[index]);
                    elems.splice(index, 1);
                }
            };
        }
      }
    })
  .directive('subpositions', function($compile){
    return {
      restrict: 'E',
      transclude: true,
        scope: { model: '=', positions: '=', placeholder: '@', add: '@', detailsCollapsed: '@'},
      link: function(scope, element, attrs) {
          if (! scope.positions)
              scope.positions = [];

          $compile('<div style="margin-top:1em"><ul positionslist model="model" positions="positions" details-collapsed="{{detailsCollapsed}}" placeholder="{{placeholder}}" add="{{add}}"></ul></div>')(scope, function(cloned, scope) {
            element.replaceWith(cloned);
          });
      }
    }
  });

function OrganizationalCtrl($scope, $http, $log) {

    $scope.load = function(currentModel) {
        $http.get(currentModel.url+"/json").success(function(data) {
            $scope.model = data.model;
            $scope.data = data;

            if (! $scope.model.roles)
                $scope.model.roles = [];

            if (! $scope.model.persons)
                $scope.model.persons = [];

            if (! $scope.model.units)
                $scope.model.units = [];
        });
    };

    $scope.$watch("navbar.currentModel", function(currentModel, oldModel) {
        $log.info("Current model: " + currentModel);
        if (currentModel)
            $scope.load(currentModel);
    });


    $scope.save = function () {
        $log.info("Saving model...");
        $log.info($scope.model);
        $http.put($scope.navbar.currentModel.url+"/json", $scope.data);
    }

}
