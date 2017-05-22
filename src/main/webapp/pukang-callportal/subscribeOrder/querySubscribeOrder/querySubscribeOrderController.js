angular.module('app')
    .controller('QuerySubscribeOrderController', function($scope, $modal, QuerySubscribeOrderService, $filter) {
        //选择时间
        // query param
        ~ function initQueryParam () {

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1,
                class: 'datepicker'
            };

            $scope.open = function($event, index) {
                $event.preventDefault();
                $event.stopPropagation();
                if (index === 0) {
                    $scope.openedBegin = true;
                } else {
                    $scope.openedEnd = true;
                }

            };

            $scope.format = 'yyyy-MM-dd';

            $scope.queryParam = {};
            $scope.queryParam.orderStates = 0;
        }();

        // datagrid
        ~ function initDataGrid() {

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
                    field: 'orderCode',
                    displayName: '流水号'
                }, {
                    field:'orderCreateTime',
                    displayName:'创建时间',
                    cellTemplate:'<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | date:"yyyy-MM-dd HH:mm:ss"}}</span></div>'
                },{
                     field:'checkupTime',
                     displayName:'预约时间'
                },{
                    field: 'merchantName',
                    displayName: '机构名',
                    width: 200
                }, {
                    field: 'userName',
                    displayName: '用户名'
                }, {
                    field: 'userTelephone',
                    displayName: '电话'
                }, {
                    field: 'orderState',
                    displayName: '预约状态',
                    cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | subscribeStatusFilter}}</span></div>'
                }, {
                    displayName: '回电',
                    width:50,
                    cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>' +
                        '<a href="javascript:void(0);" class="text-warning" ng-click="softphonebarDialout(row.entity.userTelephone)"><i class="glyphicon glyphicon-phone"></i></a></span></div>'
                }, {
                    displayName: '详情',
                    width: 75,
                    cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>' +
                    '<a href="javascript:void(0);" class="text-info" ng-click="showDetail(row)">详情</a></span></div>'
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
                    $scope.querySubscribeOrder($scope.queryParam, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                }
            }, true);
        }();

        //获取预约信息
        $scope.querySubscribeOrder = function(params, pageIndex, pageSize) {
            params.currentPage = pageIndex;
            params.perPage = pageSize;
            params.orderCreateBeginTime = $filter('date')(params.orderCreateBeginTime, 'yyyy-MM-dd');
            params.orderCreateEndTime = $filter('date')(params.orderCreateEndTime, 'yyyy-MM-dd');
            QuerySubscribeOrderService.querySubscribeOrder(params)
                .success(function(response) {
                    $scope.orderList = response.orderList;
                    console.log(response);
                })
                .error(function(XMLHttpRequest) {
                    console.log(XMLHttpRequest);
                });
        };
        //查询按钮
        $scope.submitQueryParams = function() {
            $scope.querySubscribeOrder($scope.queryParam, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
        };
        $scope.showDetail = function(row) {
            //serialNo = serialNo.entity.orderCode;
            row = row.rowIndex;
            $scope.subscribeOrderDetail = $scope.orderList[row];
            console.dir($scope.subscribeOrderDetail);
            $scope.showDetailModal();
            //QuerySubscribeOrderService.querySubscribeOrderDetail({
            //        orderCode: serialNo
            //    })
            //    .success(function(data) {
            //        console.dir(data);
            //        $scope.subscribeOrderDetail = data;
            //        $scope.showDetailModal();
            //    })
            //    .error(
            //        function(XMLHttpRequest) {
            //            console.error(XMLHttpRequest);
            //        }
            //    );
        };


        //查看预约详情
        $scope.showDetailModal = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'pukang-callportal/subscribeOrder/querySubscribeOrder/querySubscribeOrderDetail.html',
                controller: 'QuerySubscribeOrderDetailController',
                size: size,
                backdrop:'static',
                keyboard:false,
                resolve: {
                    subscribeOrderDetail: function() {
                        console.dir($scope.subscribeOrderDetail);
                        return $scope.subscribeOrderDetail;
                    }
                }
            });
        };
    });
app.controller('QuerySubscribeOrderDetailController', function($scope, subscribeOrderDetail, $modalInstance) {
    $scope.subscribeOrderDetail = subscribeOrderDetail;


    console.info(subscribeOrderDetail);

    $scope.okSubscribe = function() {
        $modalInstance.close(); //关闭并返回当前选项
    };
    $scope.cancelSubscribe = function() {
        $modalInstance.dismiss('cancel'); // 退出
    };
});
