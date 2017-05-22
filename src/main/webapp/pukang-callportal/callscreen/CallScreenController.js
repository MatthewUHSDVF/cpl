app.controller('CallScreenController',function($scope,$state,RootCallDataService, $filter, sysService,callService, $modalInstance, MakeWorkOrder, $timeout){

    // callService.callPhone({'FromCid':'15651309318'}).success(function(data){
    //    RootCallDataService.setterRingData(data);
    // }).error(function(xmlHttpRequest){
    //    console.log(xmlHttpRequest);
    // });   //test
    $scope.isReadonly = true;
    $scope.showClientMore = function () {
        $scope.isShowClientMore = !$scope.isShowClientMore;
    };
    //noinspection JSValidateTypes
    if(RootCallDataService.getter().ringData && typeof (RootCallDataService.getter().ringData) !== undefined){
        console.log(RootCallDataService.getter());
        var rootCallDataService = RootCallDataService.getter();

        $scope.ringData = rootCallDataService.ringData; //响铃数据
        console.dir($scope.ringData);
        //$scope.ringData.showstarttime = new Date();//test
        /**
         *来电时间在Data里面，回电时间在Timestamp里面
         */
        $scope.paramsRingDatas = {};
        $scope.peerData = rootCallDataService.peerData; //合力返回原始数据
        /**
         * 判断来电是否在数据库无信息
         */

        if($scope.ringData.isCrm == 2){
            $scope.paramsRingDatas.cardCode = $scope.ringData.insuCard.cardCode;
            $scope.paramsRingDatas.customerName = $scope.ringData.insuPerson.personName;
            $scope.paramsRingDatas.sex = $scope.ringData.insuPerson.personGender;
            $scope.paramsRingDatas.cardStatus = $scope.ringData.insuCardCustom.cardStatus;
        }else if ($scope.ringData.isCrm == 1){
            $scope.paramsRingDatas.cardCode = $scope.ringData.customerManage.bindCard;
            $scope.paramsRingDatas.customerName = $scope.ringData.customerManage.customerName;
            $scope.paramsRingDatas.customerType = $scope.ringData.customerManage.customerType;
            $scope.paramsRingDatas.sex = $scope.ringData.customerManage.customerSex;
        }

        $scope.ringData.sexList = $scope.ringData.crmOptionItemList.sex[0].optionItemCode;
        $scope.paramsRingDatas.handler = $scope.peerData.loginUser;
        $scope.paramsRingDatas.status = 1;
        $scope.paramsRingDatas.mobile = $scope.peerData.FromCid;
        $scope.paramsRingDatas.workorderType = 1;
        $scope.paramsRingDatas.channel = 1;
        $scope.paramsRingDatas.callStates = 1;
        if($scope.peerData.Data.RingTime && $scope.peerData.Data.RingTime !=='' && $scope.peerData.Data.RingTime !== null)
            {
                $scope.ringData.showstarttime = new Date(($scope.peerData.Data.RingTime)* 1000);
                $scope.paramsRingDatas.callRingStartTime = new Date(($scope.peerData.Data.RingTime)* 1000);
                console.dir('time', $scope.peerData.Data.RingTime);
            }else {
                $scope.ringData.showstarttime = new Date(($scope.peerData.Timestamp)* 1000);
                $scope.paramsRingDatas.callRingStartTime = new Date(($scope.peerData.Timestamp)* 1000);
                console.dir('time', $scope.peerData.Timestamp);
            }
        /**
         * 提交通话工单
         */
        function clickCtrl() {
            $timeout(function(){
                $scope.isClick = false;
            },3000)
        }
        function makeWorkOrder(isTemporaryStorage) {
            clickCtrl();
            if (!$scope.isClick) {
                $scope.isClick = true;
                $scope.paramsRingDatas.temporaryStorage = isTemporaryStorage || 0;
                MakeWorkOrder.makeWorkOrder($scope.paramsRingDatas)
                    .then(function (data) {
                        $scope.paramsRingDatas = {};
                        $modalInstance.close();
                        if (data){
                            sysService.showMessage('提交工单成功！');
                        }
                    }, function () {
                        sysService.showMessage('提交失败！');
                    });
            }
        }
        $scope.submitRingData = makeWorkOrder;
        $scope.temporaryStorage = function(){
            makeWorkOrder(1);
        };
    }else {
        $modalInstance.dismiss('cancel');
    }
});
/**
 * CallScreenInfoController
 */
app.controller('CallScreenInfoController',function($scope,$state){
    console.log($scope);
});
