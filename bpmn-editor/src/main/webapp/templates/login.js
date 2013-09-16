angular.module('loginModule', ['ui.bootstrap'])
  .factory('loginService', ['$http', '$rootScope', function($http, $rootScope) {
     var LoginHandler = {
        _isLogged: false,
        _user: "",
        _baseUrl: "service",

        init: function ()  {
            if (typeof ORYX != "undefined")
                this._baseUrl = "../service";

            LoginHandler.currentUser();
        },

        _hasLoggedIn: function(user) {
            LoginHandler._isLogged = true;
            LoginHandler._user = user;
            $rootScope.$emit('login', user);
        },

        _hasLoggedOut: function() {
            LoginHandler._isLogged = false;
            LoginHandler._user = "";
            $rootScope.$emit('logout');
        },

        currentUser: function () {
            return $http.get(this._baseUrl + "/user").
                success(function(data) {
                    LoginHandler._hasLoggedIn(data);
                }).
                error(function (data) {
                    LoginHandler._hasLoggedOut();
                });
        },

        logout: function () {
            return $http.post(this._baseUrl + "/user/logout").
                success(function(data) {
                    LoginHandler._hasLoggedOut();
                });
        },

        isLogged: function() {
            return this._isLogged;
        }
    };

    LoginHandler.init();

    return LoginHandler;

  }])
  .directive('login', ['loginService', '$rootScope', function($login, $rootScope){
    return {
      restrict: 'C',
      replace: true,
      transclude: true,
      scope: {username: '@title'},
      template: '<li>' +
                  '<a href="" class="dropdown-toggle">{{username}} <b class="caret"></b></a>' +
                  '<div class="dropdown-menu" style="padding: 15px" ng-transclude>' +
                  '</div>' +
                '</li>',
      link: function(scope, element, attrs) {
        var logged = false;
        var logOutMenu = angular.element(element.children()[1]);
        var logout = angular.element('<a id="logout" href="#"><i class="icon-off"></i> Logout</a>');
        var menu = angular.element('<ul class="dropdown-menu"></ul>').append(angular.element("<li></li>").append(logout));

        logout.on('click', function() {
            $login.logout();
        });

        $rootScope.$on('login', function(event, user) {
            if (! logged) {
                logged = true;
                scope.username = user;
                logOutMenu.replaceWith(menu);
            }
        });

        $rootScope.$on('logout', function(event) {
            if (logged) {
                logged = false;
                scope.username = scope.title;
                menu.replaceWith(logOutMenu);
            }
        });
      }
    }
  }]);