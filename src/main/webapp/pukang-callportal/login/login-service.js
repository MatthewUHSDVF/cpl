/**
 * Created by jason on 16/5/30.
 */
angular.module('app')
    .service('LoginService', function ($http) {
        var loginService = {};
        loginService.login =  function(user){
            var methodName = 'login';
            return $http({
                method: 'post',
                url: 'checkUser',
                params: user
            });
        };

        loginService.logoutOut = function(user,PKO_CONFIG){
            var methodName = 'logout';
            console.log(methodName,user);
            return $http({
                method: 'post',
                url: PKO_CONFIG.PKOperateServerIP.concat('logout'),
                params: {userName:user.userName,userPassword:user.userPassword},
            });
        };
        return loginService;
    });