/**
 * Created by slipkinem on 2016/9/23.
 */
angular.module('app').
    service('QueryWorkOrderService',function($http,PKO_CONFIG,sysService){
        this.queryWorkOrder = function(postData){
            console.info(postData);
            return $http({
                url:'selectListByWorkorderQueryVo',
                method:'POST',
                params:postData
            })
        };
        this.showDetail = function(postData){
            return $http({
                url:'selectInfo',
                method:'POST',
                params:{'serialNo':postData}
            })
        };
        this.deleteWorkOrderBySerialNo = function(postData){
            return $http({
                url:'deleteWorkOrderBySerialNo',
                method:'POST',
                params:postData
            })
        };
        this.downLoadWorkOrder = function (postData) {
            var url = PKO_CONFIG.PKCallPortalServerIP.concat('downLoadWorkOrder');
            sysService.downloadFile(url,postData);
        };
        this.updataWorkOrder = function(postData){
            return $http({

            })
        }
    });