/**
 * Created by jason on 16/5/30.
 */

'use strict';

/* Controllers */
// signin controller
app.controller('LoginController', function($scope, $http, $state, LoginService,Session, locals) {
    $scope.user = {};
    $scope.user.userName = '';
    $scope.user.userPassword = '';
    $scope.authError = null;

    $scope.login = function() {
        $scope.authError = null;
        // Try to login
        LoginService.login($scope.user).success(function(data){
                if (data.errorCode == 0){
                    locals.set('loginUser',$scope.user.userName);
                    locals.set('loginPassword',$scope.user.userPassword);
                    hollyglobal.loginUser = $scope.user.userName;
                    hollyglobal.loginPassword = $scope.user.userPassword;
                    Session.create(1,$scope.user.userName,'admin');
                    $state.go('app.callScreenInfo');
                }
            }).error(function(err){
                throw err;
        });
    };

    // $scope.logout = function(){
    //   LoginService.logout(null).success(function(data){
    //       Session.destroy();
    //       locals.clear();
    //       console.log(locals);
    //       $state.go('login');
    //   });
    // };
});