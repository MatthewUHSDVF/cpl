/**
 * Created by slipkinem on 2016/9/23.
 */
angular.module('app')
    .controller('NewWorkOrderController', function($scope, $modal, callService, sysService, $log, $filter, $q, $timeout,CommonDataList,ShowAddClientModal, MakeWorkOrder) {
        /**
         * 初始化数据
         * @type {{}}
         */
        $scope.addClientParams = {};
        $scope.toggleTpl = {};
        $scope.isReadonly = false;

        $scope.paramsRingDatas = {};
        $scope.ringData = {};
        function init() {
            $scope.paramsRingDatas.workorderType = 1;
            $scope.paramsRingDatas.status = 1;
            $scope.paramsRingDatas.channel = 1;
            $scope.paramsRingDatas.callStates = 1;
            $scope.paramsRingDatas.sex = 1;
            $scope.paramsRingDatas.customerType = 1;
        }
        init();

        var promise = CommonDataList.getCommonDataList();
        promise.then(function (data) {
            //$scope.ringData = data;
            $scope.ringData.crmOptionItemList = data;
            //$scope.crmList = data.crmOptionItemList[0];
            //console.log($scope.crmList);

            console.log($scope.ringData);
            });
            //.then(function () {
            //    $scope.ringData.sexList = [{'sexCode':'1'},{'sexCode':'2'}];
            //});
        /**
         * ng-grid配置
         */
        function initDataGrid() {

            // 行
            $scope.columnDefs = [
                {
                    field: 'customerName',
                    displayName: '姓名',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field)}}</div>'
                }, {
                    field:'customerPhone',
                    displayName:'电话',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field)}}</div>'
                },{
                    field: 'customerSex',
                    displayName: '性别',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field) | sexFilter}}</div>'
                }, {
                    field: 'bindCard',
                    displayName: '卡号',
                    cellTemplate:'<div class="ngCellText" >{{row.getProperty(col.field)}}</div>'
                }, {
                    field: 'customerType',
                    displayName: '客户类别',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field) | customerTypeFilter}}</div>'
                }, {
                    field: 'customerAddress',
                    displayName: '地址',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field)}}</div>'
                }, {
                    field: 'customerEmail',
                    displayName: '邮箱',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field)}}</div>'
                }, {
                    field: 'weixin',
                    displayName: 'weixin',
                    cellTemplate:'<div class="ngCellText">{{row.getProperty(col.field)}}</div>'
                }, {
                    displayName: '创建工单',
                    cellTemplate: '<div class="ngCellText"><a href="javascript:void(0);" class="text-info" ng-click="newWorkOrderClick(row)">创建</a></div>'
                }
            ];


            $scope.newWorkOrderClick = function (row) {
                $scope.toggleTpl.isShowNewOrder = true;
                $scope.paramsRingDatas.mobile = row.entity.customerPhone;
                $scope.paramsRingDatas.cardCode = row.entity.bindCard;
                $scope.paramsRingDatas.sex = row.entity.customerSex;
                $scope.paramsRingDatas.customerName = row.entity.customerName;
                $scope.paramsRingDatas.customerAddress = row.entity.customerAddress;
                $scope.paramsRingDatas.customerType = row.entity.customerType;
            };

            // gird options
            $scope.gridOptions = {
                data: 'orderList',
                columnDefs: $scope.columnDefs,
            };
            console.log('result',$scope.orderList);
        }
        initDataGrid();
        /**
         * 查询客户
         */
        function fnQueryClient(request) {
            callService.queryClient(request)
                .success(function (data) {
                    if (data.errorCode == 0){
                        if (data.customerManages){
                            $scope.orderList = data.customerManages;
                            $scope.toggleTpl.isShowNewOrder = false;
                            //sysService.showMessage('查询成功');
                        }else {
                            sysService.showMessage('错误');
                        }
                    }else {
                        sysService.showMessage(data.errorMessage,'失败：');
                    }
                })
                .error(function (e) {
                    sysService.showMessage('查询失败');
                    throw e;
                })
        }
        $scope.queryClient = function () {
            fnQueryClient($scope.addClientParams)
        };

        $scope.showClientMore = function () {
            $scope.isShowClientMore = !$scope.isShowClientMore;
        };
        function clickCtrl() {

            $timeout(function(){
                $scope.isClick = false;
            },3000)
        }

        /**
         * 提交工单
         * @param isTemporaryStorage
         */
        function makeWorkOrder(isTemporaryStorage) {
            if (!$scope.isClick) {
                $scope.isClick = true;
                $scope.paramsRingDatas.isTS = isTemporaryStorage || 0;
                MakeWorkOrder.makeWorkOrder($scope.paramsRingDatas)
                    .then(function (data) {
                        if (data) {
                            sysService.showMessage('提交工单成功！');
                            $scope.paramsRingDatas = {};
                            $scope.toggleTpl.isShowNewOrder = false;
                        }else {
                            sysService.showMessage('提交失败！');
                        }
                    }, function () {
                        sysService.showMessage('提交失败！');
                    })
                    .then(init)
                    .then(clickCtrl);
            }
        }
        $scope.submitRingData = makeWorkOrder;

        /**
         * 添加客户
         */
        $scope.showAddClientModal = function () {
            $scope.toggleTpl.isShowNewOrder = false;
            ShowAddClientModal.showAddClientModal($scope.ringData)
                .result.then(function (modalData) {
                if (modalData){
                    $scope.orderList = modalData;
                    fnQueryClient({customerPhone:modalData.customerPhone});
                }
            });
        }
    });