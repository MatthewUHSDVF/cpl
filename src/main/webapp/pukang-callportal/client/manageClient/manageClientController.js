/**
 * Created by slipkinem on 2016/9/23.
 */
   app.controller('ManageClientController', function($scope,callService,CommonDataList,ShowAddClientModal,ShowUpdateClientModal,sysService) {

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
       /**
        *请求通用List
        */
       var promise = CommonDataList.getCommonDataList();
       promise.then(function (data) {
           $scope.ringData = data;
            })
           .then(function () {
               $scope.ringData.sexList = [{'sexCode':'1'},{'sexCode':'2'}];
           });

        function initDataGrid() {

           // 分页
           $scope.pagingOptions = {
               pageSizes: [10, 20, 30],
               pageSize: 10,
               currentPage: 1
           };

           // 排序
           $scope.filterOptions = {
               filterText: "",
               useExternalFilter: true
           };

           // 行
           $scope.columnDefs = [
               {
                   field: 'customerName',
                   displayName: '姓名',
                   cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               }, {
                   field:'customerPhone',
                   displayName:'电话',
                   cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               },{
                   field: 'customerSex',
                   displayName: '性别',
                   cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | sexFilter}}</span></div>'
               }, {
                   field: 'bindCard',
                   displayName: '卡号',
                   cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               }, {
                   field: 'customerType',
                   displayName: '客户类别',
                   cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | customerTypeFilter}}</span></div>'
               }, {
                   field: 'customerAddress',
                   displayName: '地址',
                   cellTemplate:'<div class="ngCellText" ng-hide="true" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               }, {
                   field: 'customerEmail',
                   displayName: '邮箱',
                   cellTemplate:'<div class="ngCellText" ng-hide="true" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               }, {
                   field: 'weixin',
                   displayName: '微信',
                   cellTemplate:'<div class="ngCellText" ng-hide="true" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field)}}</span></div>'
               }, {
                   displayName: '编辑',
                   cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>' +
                   '<a href="javascript:void(0);" class="text-info" ng-click="editClient(row)">编辑</a></span></div>'
               }
           ];
           // gird options
           $scope.gridOptions = {
               data: 'orderList',
               enablePaging: true,
               showFooter: true,
               totalServerItems: 'totalServerItems',
               pagingOptions: $scope.pagingOptions,
               filterOptions: $scope.filterOptions,
               columnDefs: $scope.columnDefs,
               multiSelect: false
           };
           // 分页 watch
           $scope.$watch('pagingOptions', function(newVal, oldVal) {
               if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                   $scope.queryClient($scope.queryParam);
               }
           }, true);
       }
       initDataGrid();
       /**
        * 提交工单
        */
       function fnQueryClient(request) {
           callService.queryClient(request)
               .success(function (data) {
                   if (data.errorCode != 1){
                       if (data.customerManages){
                           $scope.orderList = data.customerManages;
                       }else {
                           sysService.showMessage('没有查出，请添加客户')
                       }
                   }else {
                       sysService.showMessage(data.errorMessage);
                   }
               })
               .error(function (e) {
                   console.log(e);
                   sysService.showMessage('查询失败')
               })
       }
       $scope.queryClient = function () {
           fnQueryClient($scope.addClientParams);
       };
       /**
        *添加客户
        */
       $scope.showAddClientModal = function () {
           ShowAddClientModal.showAddClientModal($scope.ringData)
               .result.then(function (data) {
               if (data){
                   fnQueryClient({'customerPhone':data.customerPhone});
               }
           });
       };
       /**
        * 编辑客户
        * @param row
        */
       $scope.editClient = function (row) {
           $scope.ringData.editData = {};
           angular.extend($scope.ringData.editData,row.entity);
           console.dir($scope.ringData);
           ShowUpdateClientModal.showUpdateClientModal($scope.ringData).result
               .then(function (data) {
               if (data){
                   fnQueryClient({'customerPhone':data.customerPhone});
               }
           });
       }
    });