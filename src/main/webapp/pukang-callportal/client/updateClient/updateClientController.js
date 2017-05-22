/**
 * Created by slipkinem on 2016/9/23.
 */
   app.controller('UpdateClientController', function($scope,commonData,$modalInstance,callService,sysService, $timeout,CommonDataList) {
       if (commonData.hasOwnProperty('editData')){
           $scope.clientParams = commonData.editData;
       }

       //$scope.clientData = commonData;

       function clickCtrl() {
           $timeout(function(){
               $scope.isClick = false;
           },3000)
       }

       var promise = CommonDataList.getCommonDataList();
       promise.then(function (data) {
           $scope.clientData = data;
           console.log($scope.clientData);
       });

       /**
        * 关闭modal
        */
        $scope.ok = function () {
            clickCtrl();
            if (!$scope.isClick) {
                callService.updateCilent($scope.clientParams)
                    .success(function (data) {
                        if (data){
                            $modalInstance.close($scope.clientParams);
                            if (data == 1) {
                                sysService.showMessage('更新成功')
                            }
                        }else {
                            sysService.showMessage('更新失败，原因：' + data.errorMessage);
                        }
                    })
                    .error(function (e) {
                        console.error(e);
                        sysService.showMessage('更新失败')
                    });
            }
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    });