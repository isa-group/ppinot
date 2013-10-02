angular.module('listHandling', ['ui.bootstrap'])
    .directive('elementlist', function(){
        return {
            restrict: 'A',
            replace: true,
            transclude: true,
            scope: { ngModel: '=', placeholder: '@', add: '@', showAdd: '@'},
            template: '<ul>' +
                      '<li ng-repeat="el in ngModel" ng-transclude>' +
                         '<input type="text" class="templatefield" ng-model="el.name" placeholder="{{placeholder}}" required/>'+
                         '<a remove from="ngModel" index="{{$index}}"></a>'+
                      '</li>' +
                      '<li ng-show="showAdd"><a class="btn" ng-click="addElem(ngModel)"><i class="icon-pencil"></i> {{add}}</a></div></li>'+
                      '</ul>',
            link: function($scope, $element) {
                $scope.addElem = function(elems) {
                    elems.push({name:""});
                }

                $scope.removeElem = function(elems, index) {
                    elems.splice(index, 1);
                }
            }
        }
    })
    .directive('remove', function() {
        return {
            restrict: 'A',
            replace: true,
            transclude: true,
            scope: {from: '=', index: '@'},
            template: '<a class="btn-mini" ng-click="removeElem(from, index)"><i class="icon-trash"></i><span ng-transclude></span></a>',
            link: function(scope, element) {
                scope.removeElem = function(elems, index) {
                     elems.splice(index, 1);
                };
            }
        }
    })
    .directive('addform', function () {
        return {
            restrict: 'A',
            replace: false,
            scope: {placeholder: '@', button: '@', list: '='},
            template: '<div class="input-append">'+
                          '<input type="text" ng-model="newName" placeholder="{{placeholder}}"/>'+
                          ' <button class="btn" type="submit" ng-click="add()"><i class="icon-plus"></i> {{button}}</button>'+
                      '</div>',
            link: function (scope, element) {
                element.addClass("form-inline");
                scope.add = function() {
                    if (! scope.list)
                        scope.list = [];

                    if (scope.newName != "") {
                        scope.list.push({name: scope.newName});
                        scope.newName = "";
                    }
                }
            }
        }
    });

