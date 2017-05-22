/**
 * Created by jason on 16/6/2.
 * upload shop list dialog controller
 */

angular.module('app')
    .controller('PKShowMessageController',function($scope,$log,$modalInstance){

        if (!$scope.title){
            $scope.title = '提醒';
        }

        $scope.response = [];

        $scope.btnOKClick = function(){
            try{
                $modalInstance.close($scope.response);
            } catch(err){
                console.info(err);
            }
        };
    });
