/**
 * Created by slipkinem on 2016/9/23.
 */
   app.controller('AddClientController', function($scope,commonData,$modalInstance,callService,sysService,CommonDataList, $timeout) {
       $scope.clientParams = {
           customerType: 1,
           customerSex: 1
       };
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
                callService.addCilent($scope.clientParams)
                    .success(function (data) {
                        if (data){
                            $modalInstance.close($scope.clientParams);
                            sysService.showMessage('添加成功')
                        }else {
                            sysService.showMessage('添加失败，原因：' + data.errorMessage);
                        }
                    })
                    .error(function (e) {
                        console.error(e);
                        sysService.showMessage('添加失败')
                    });
            }
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    });