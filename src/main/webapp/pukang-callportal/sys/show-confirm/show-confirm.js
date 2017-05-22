/**
 * Created by jason on 16/6/2.
 * upload shop list dialog controller
 */

angular.module('app')
    .controller('PKShowConfirmController',function($scope,$log,$modalInstance){

        // init
        if (!$scope.title){
            $scope.title = '提醒';
        }

        $scope.response = {};

        // 确定
        $scope.btnOKClick = function(){
            try{
                $scope.response = {errorCode: 0,errorMessage:'确定'};
                $modalInstance.close($scope.response);
            } catch(err){
                console.info(err);
            }
        };

        // 取消
        $scope.btnCancelClick = function(){
            $modalInstance.dismiss('cancel');
        };
    });
