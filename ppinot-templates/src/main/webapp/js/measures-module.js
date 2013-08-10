angular.module('measuresModule', [])
  .directive('measure', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      require: '?ngModel',
      scope: { ngModel:'=', process: '=' },
      template: '<span ng-switch="ngModel.kind">' +
                  '<span ng-switch-when="CountInstanceMeasure">the number of times <event ng-model="ngModel.when" process="process"></event></span>' +
                  '<span ng-switch-when="TimeInstanceMeasure">the duration between the time instants when <event ng-model="ngModel.from" process="process"></event> and when <event ng-model="ngModel.to" process="process"></event></span>' +
                  '<span ng-switch-when="DataInstanceMeasure">the value <span ng-hide="ngModel.dataContentSelection.selection==\'\'">of {{ngModel.dataContentSelection.selection}}</span> of {{process.id[ngModel.dataContentSelection.dataobjectId].name}}</span>' +
                  '<span ng-switch-when="DataPropertyConditionInstanceMeasure">{{ngModel.condition.statesConsidered.stateString}} {{process.id[ngModel.condition.appliesTo].name}} that satisfies: {{ngModel.condition.restriction}}</span>' +
                  '<span ng-switch-when="StateConditionInstanceMeasure">{{process.id[ngModel.condition.appliesTo].type}} {{process.id[ngModel.condition.appliesTo].name}} that is currently or has finished {{ngModel.condition.state.stateString}}</span>' +
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
          $compile("<span>the {{aggFunction}} of <measure ng-model='baseMeasure' process='process'></measure></span>")(scope, function(cloned, scope) {
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
        var template = '<span>the {{derived.function}}, where: <ul>' +
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
      template: '<span>{{process.id[ngModel.appliesTo].type}} {{process.id[ngModel.appliesTo].name}} becomes {{ngModel.changesToState.stateString}}</span>'
    }
  });