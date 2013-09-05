angular.module('measuresModule', ['ui.bootstrap'])
  .directive('measure', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      require: '?ngModel',
      scope: { ngModel:'=', process: '=' },
      template: '<span ng-switch="ngModel.kind">' +
                  '<span ng-switch-when="CountMeasure">the number of times <event ng-model="ngModel.when" process="process"></event></span>' +
                  '<span ng-switch-when="TimeMeasure">the duration between the time instants when <event ng-model="ngModel.from" process="process"></event> and when <event ng-model="ngModel.to" process="process"></event></span>' +
                  '<span ng-switch-when="DataMeasure">the value <span ng-hide="ngModel.dataContentSelection.selection==\'\'">of {{ngModel.dataContentSelection.selection}}</span> of {{process.id[ngModel.dataContentSelection.dataobjectId].name}}</span>' +
                  '<span ng-switch-when="DataPropertyConditionMeasure">{{ngModel.condition.statesConsidered.stateString}} {{process.id[ngModel.condition.appliesTo].name}} that satisfies: {{ngModel.condition.restriction}}</span>' +
                  '<span ng-switch-when="StateConditionMeasure">{{process.id[ngModel.condition.appliesTo].type}} {{process.id[ngModel.condition.appliesTo].name}} that is currently or has finished {{ngModel.condition.state.stateString}}</span>' +
                  '<span ng-switch-when="AggregatedMeasure"><aggregatedMeasure base-measure="ngModel.baseMeasure" agg-function="ngModel.aggregationFunction" process="process"></aggregatedMeasure></span>' +
                  '<span ng-switch-when="DerivedSingleInstanceMeasure"><derivedMeasure derived="ngModel" process="process"></derivedMeasure></span>' +
                  '<span ng-switch-when="DerivedMultiInstanceMeasure"><derivedMeasure derived="ngModel" process="process"></derivedMeasure></span>' +
                  '<span ng-switch-default>{{ngModel.kind}}</span>' +
                '</span>'
    }
  })
  .directive('aggregatedmeasure', function($compile){
    return {
      restrict: 'E',
      transclude: true,
      scope: {aggFunction: '=', baseMeasure: '=', process:'='},
      link: function(scope, element, attrs) {
        if (scope.baseMeasure != null) {
          $compile("<span>the {{aggregationFunction}} of <measure ng-model='baseMeasure' process='process'></measure></span>")(scope, function(cloned, scope) {
            element.replaceWith(cloned);
          })
        }
      }
    }
  })
  .directive('derivedmeasure', function($compile){
    return {
      restrict: 'E',
      transclude: true,
      scope: {derived: '=', process: '='},
      link: function(scope, element, attrs) {
        var template = '<span> {{derived.function}}, where: <ul>' +
                        '<li ng-repeat="(v, def) in derived.usedMeasureMap">' +
                           '{{v}} is <measure ng-model="def" process="process"></measure>' +
                        '</li>' +
                       '</ul></span>';
        $compile(template)(scope, function(cloned, scope) {
          element.replaceWith(cloned);
        })
      }
    }
  })
  .directive('event', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      scope: { ngModel: '=', process: '='},
      template: '<span>{{process.id[ngModel.appliesTo].type}} {{process.id[ngModel.appliesTo].name}} becomes {{stateName(ngModel.changesToState)}}</span>',
      controller: function($scope) {
        $scope.stateName = function(changesToState) {
            var stateName = "";
            angular.forEach(changesToState, function (value, key) {
                stateName = value;
            });
            return stateName;
        };
      }
    }
  })
  .directive('elementlist', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      scope: { ngModel: '=', placeholder: '=', add: '='},
      template: '<ul>' +
                  '<li ng-repeat="el in ngModel">' +
                     '<input type="text" class="templatefield" ng-model="el.elem" placeholder="{{placeholder}}" />'+
                     '<a class="btn-mini" ng-click="removeElem(ngModel, $index);"><i class="icon-trash"></i></a>'+
                  '</li>' +
                  '<li>' +
                     '<a class="btn-mini" ng-click="addElem(ngModel);">{{add}}</a>' +
                  '</li>' +
                '</ul>',
      controller: function($scope, $element) {
        $scope.addElem = function(elems) {
            elems.push({elem:""});
        }

        $scope.removeElem = function(elems, index) {
            elems.splice(index, 1);
        }
      }
    }
  })
  .directive('target', function() {
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      scope: { ngModel: '='},
      template: '<span><span>{{tpattern(ngModel.refMin, ngModel.refMax)}}</span> ' +
                '<a ng-click="toggleEdit()" tooltip="Edit target value"><i class="icon-pencil"></i></a>' +
                '<form class="form-inline" ng-show="editTarget">' +
                    '<input type="text" class="input-small" placeholder="Lower bound" ng-model="ngModel.refMin"/> '+
                    '<input type="text" class="input-small" placeholder="Upper bound" ng-model="ngModel.refMax"/> ' +
                '</form></span>',
      controller: function($scope, $element) {
        $scope.editTarget = false;
        $scope.toggleEdit = function() {
            $scope.editTarget = !$scope.editTarget;
        }
        $scope.tpattern = function(min, max) {
          var isEmpty = function (val) { return val == null || val == ""};
          var result = "...";

          if (! isEmpty(min) && isEmpty(max))
            result = "greater than "+min;
          else if (isEmpty(min) && ! isEmpty(max))
            result = "lower than "+max;
          else if (! isEmpty(min) && ! isEmpty(max)) {
            if (min == max)
              result = min;
            else 
              result = "between "+min + " and "+max;            
          }

          return result;
        }

      }

    }
  });