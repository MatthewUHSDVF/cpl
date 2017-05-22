/**
 * Created by slipkinem on 2016/9/22.
 */
angular.module('app').service('CallScreenService',function($http){
    this.submitRing = function(data){
        return $http({
            url:'/PKCallPortal/commitWorkOrder',
            method:'POST',
            params:data
        })
    }
});