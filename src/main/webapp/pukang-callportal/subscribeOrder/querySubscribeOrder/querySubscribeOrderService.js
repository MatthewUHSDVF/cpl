angular.module('app')
    .service('QuerySubscribeOrderService',function($http){
        this.querySubscribeOrder = function(request){
            return $http({
                url:'getMECOrderByQueryOrderListVo',
                method:'POST',
                params:request
            })
        };
        this.querySubscribeOrderDetail = function (request) {
            return $http({
                url:'subscribeInfoByOrderCode',
                method:'POST',
                params:request
            })
        }

    });