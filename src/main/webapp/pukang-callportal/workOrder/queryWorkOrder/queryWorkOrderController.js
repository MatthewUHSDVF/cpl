/**
 * Created by slipkinem on 2016/9/23.
 */
angular.module('app')
    .controller('queryWorkOrderController', function($scope, $modal, QueryWorkOrderService, sysService, $log, $filter, $q, $window) {
        //选择时间
        // query param
        ~ function() {

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
        }();

        // datagrid
        ~ function() {

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
            $scope.columnDefs = [{
                field: 'serialNo',
                displayName: '流水号',
                width: 200
            }, {
                field: 'customerName',
                displayName: '姓名'
            }, {
                field: 'mobile',
                displayName: '电话',
                width: 200
            }, {
                field: 'cardCode',
                displayName: '卡号',
                width: 200
            }, {
                field: 'workorderType',
                displayName: '工单类型',
                cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | workOrderTypeFilter}}</span></div>'
            }, {
                field: 'handler',
                displayName: '处理人'
            }, {
                field: 'status',
                displayName: '处理状态',
                cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.getProperty(col.field) | finishedFilter}}</span></div>'
            }, {
                field: 'soundRecording',
                displayName: '录音',
                cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>' +
                    '<a ng-click="playRecord(row)" href="javascript:void(0)"><i class="glyphicon glyphicon-play-circle"></i></a></span></div>'
            }, {
                displayName: '详情',
                cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text="">' +
                    '<a href="javascript:void(0);" class="text-info" ng-click="showDetail(row)">详情</a></span></div>'
            }];

            // gird options
            $scope.gridOptions = {
                data: 'workOrderLists',
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
                    $scope.queryWorkOrder($scope.queryParam, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                }
            }, true);
        }();
        //显示详情
        //function show$q(a) {
        //    var deferred = $q.defer();
        //    QueryWorkOrderService.showDetail(a)
        //        .success(function(data) {
        //            console.log(data);
        //            deferred.resolve(data);
        //        })
        //        .error(function(e) {
        //            deferred.reject(e);
        //        });
        //    return deferred.promise;
        //}

        $scope.showDetail = function(row) {
            console.dir(row);
            $scope.workOrderDetail = row.entity;
            $scope.showDetailModal();

            //var paramserialNo = serialNo.entity.serialNo;
            //var promise = show$q(paramserialNo);
            //promise.then(function(data) {
            //        $scope.workOrderDetail = data;
            //    })
            //    .then($scope.showDetailModal);


            // QueryWorkOrderService.showDetail(paramserialNo)
            //     .success(function(data){
            //         console.log(data);
            //         $scope.workOrderDetail = data;
            //         $scope.showDetailModal();
            //         console.log($scope.workOrderDetail)
            //     })
            //     .error(function(XMLHttpRequest){
            //         console.log(XMLHttpRequest);
            //     })
        };
        //详情
        $scope.showDetailModal = function(size) {
            var modalInstance = $modal.open({
                templateUrl: 'pukang-callportal/workOrder/queryWorkOrder/queryWorkOrderDetail.html',
                controller: 'QueryWorkOrderDetailController',
                size: size,
                resolve: {
                    workOrderDetail: function() {
                        return $scope.workOrderDetail;
                    }
                }
            });
        };
        /**
         * 查询工单
         * @param params
         * @param pageIndex
         * @param pageSize
         */
        $scope.queryWorkOrder = function(params, pageIndex, pageSize) {
            /**
             * 参数对象传值为半引用，防止下载时page,rows污染$scope.queryParam;
             * @type {*}
             */
            $scope.postData = angular.copy(params);

            $scope.postData.page = pageIndex;
            $scope.postData.rows = pageSize;
            params.createFromTime = $filter('date')(params.createFromTime, 'yyyy-MM-dd');
            params.createToTime = $filter('date')(params.createToTime, 'yyyy-MM-dd');
            $scope.postData.createFromTime = params.createFromTime;
            $scope.postData.createToTime = params.createToTime;
            QueryWorkOrderService.queryWorkOrder($scope.postData)
                .success(function(response) {
                    $scope.workOrderLists = response;
                })
                .error(function() {
                    console.error("获取数据发生错误");
                });
        };

        /**
         * 提交查询工单func
         */
        $scope.submitQueryParams = function() {
            $scope.queryWorkOrder($scope.queryParam, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
        };
        /**
         * 删除工单
         */
        $scope.deleteWorkOrder = function() {

            if ($scope.gridOptions.$gridScope.selectedItems.length <= 0) {
                sysService.showMessage('请先鼠标点击选中相关条目.');
                return;
            }

            var title = '确认删除';
            var message = '该工单删除后不可恢复，确认删除吗?';
            sysService.showConfirm(message, title).result.then(
                function(result) {
                    $log.debug(result);
                    console.log($scope.gridOptions.$gridScope.selectedItems);
                    QueryWorkOrderService.deleteWorkOrderBySerialNo({
                            serialNo: $scope.gridOptions.$gridScope.selectedItems[0].serialNo
                        })
                        .success(function(data) {
                            if (data.errorCode === 0) {
                                $log.debug('deleteSlipChange.success', data);
                                sysService.showMessage('删除成功');
                                $scope.submitQueryParams();
                            } else {
                                sysService.showMessage('错误信息: ' + data.errorMessage, '失败');
                            }
                        });
                },
                function(error) {
                    $log.debug(error);
                });
        };
        /**
         * 上传工单
         * @return {[type]} [description]
         */
        $scope.uploadWorkOrderInExcel = function() {
            var modalInstance = sysService.uploadFile('单据上传', 'uploadWorkOrderInExcel', []);
            modalInstance.result.then(function(response) {
                var result = response.pop();
                console.info('modal success', result);
                if (result.errorCode === 0) {
                    sysService.showMessage('业务单据上传成功!', '执行成功');
                } else {
                    sysService.showMessage('错误信息:' + result.errorMessage, '执行失败');
                }
            }, function(data) {
                console.info('Modal dismissed at: ' + new Date(), data);
                sysService.showMessage('未知错误:\r\n' + data, '错误');
            });
        };
        /**
         * 下载工单
         * @return {[type]} [description]
         */
        $scope.downLoadWorkOrder = function() {
            console.info($scope.queryParam);

            QueryWorkOrderService.downLoadWorkOrder($scope.queryParam);
        };
        /**
         * 播放录音
         * @param url
         */
        $scope.playRecord = function(url) {
            var src = url.entity.soundRecording;
            src ? sysService.showMessage('<div><audio controls src="' + src + '"></audio></div>', '播放录音') : sysService.showMessage('没有录音文件');
        };
    });

//QueryWorkOrderDetailController
app.controller('QueryWorkOrderDetailController', function($scope, $modalInstance, workOrderDetail) {
    $scope.workOrderDetail = workOrderDetail;
    $scope.ok = function() {
        $modalInstance.close(); //关闭并返回当前选项
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel'); // 退出
    };
});