/**
 * Created by slipkinem on 2016/9/22.
 */
angular.module('app')
    .service('callService',function($http){
        this.callPhone = function(postData) {
            console.log(postData);
            return $http({
                url: "/PKCallPortal/crmWake",
                method: "POST",
                params: {'FromCid':postData}
            })
        };
        this.submitRing = function(data){
            return $http({
                url:'/PKCallPortal/commitWorkOrder',
                method:'POST',
                params:data
            })
        };
        this.addCilent = function (params) {
            return $http({
                url:'insertCustomerManage',
                method:'POST',
                params:params
            })
        };
        this.updateCilent = function (params) {
            return $http({
                url:'updateCustomerManage',
                method:'POST',
                params:params
            })
        };
        this.queryClient = function (params) {
            return $http({
                url:'queryCustomerManage',
                method:'POST',
                params:params
            })
        }
})
    .factory('CommonDataList',function ($http,$q) {
        var commonDataList = {};
        var deferred = $q.defer();
        return{
            getCommonDataList: function () {
                if (angular.equals({}, commonDataList)){
                    $http({
                        url:'getOptionItemList',
                        method:'POST'
                    })
                    .success(function (data) {
                        commonDataList = data;
                        deferred.resolve(data);
                    })
                    .error(function (e) {
                        console.log(e);
                        deferred.reject(e);
                    })
                }else {
                    deferred.resolve(commonDataList);
                }
                return deferred.promise
            }
        }
    })
    .factory('RootCallDataService', function () {
        var RootCallData = {};

        function _getter() {
            console.log(RootCallData);
            return RootCallData;
        }

        function _setterRingData( data ) {
            RootCallData.ringData = data;  //普康返回响铃数据
        }

        function _setterTalkingData(data) {
            RootCallData.talkingData = data;  //合力通话数据
        }

        function _setterPeerData(data){
            RootCallData.peerData = data;    //合力返回响铃数据
        }

        function _setterHangupData(data){
            RootCallData.hungupData = data; //合力挂断数据
        }

        return {
            getter: _getter,
            setterRingData: _setterRingData,
            setterTalkingData:_setterTalkingData,
            setterPeerData:_setterPeerData,
            setterHangupData:_setterHangupData
        }
    })
    .factory('MakeWorkOrder', function (callService,RootCallDataService,sysService,$q,$filter) {
            var deferred = $q.defer();
            var rootCallDataService = RootCallDataService.getter();
        return {
            makeWorkOrder : function (params) {
                if(params.feedback !==''){
                    var hungupData = rootCallDataService.hungupData; //挂断数据
                    var talkingData = rootCallDataService.talkingData; //通话数据
                    var peerData = rootCallDataService.peerData; //通话数据
                    console.dir(hungupData);

                    var callRingEndTime;
                    if (hungupData && hungupData.hasOwnProperty('Timestamp')){
                        callRingEndTime = $filter('date')((hungupData.Timestamp)*1000,'yyyy-MM-dd HH:mm:ss');
                    }else {
                        callRingEndTime = '';
                    }

                    var begintime;
                    if (hungupData && hungupData.hasOwnProperty('Data')){
                        begintime = $filter('date')((hungupData.Data.BeginTime)*1000,'yyyy-MM-dd HH:mm:ss');
                    }else if(peerData && peerData.hasOwnProperty('Timestamp')){
                        begintime = $filter('date')((peerData.Timestamp)*1000,'yyyy-MM-dd HH:mm:ss');
                    } else {
                        begintime = '';
                    }

                    var endTime = hungupData && hungupData.hasOwnProperty('Timestamp')?$filter('date')((hungupData.Timestamp)*1000,'yyyy-MM-dd HH:mm:ss'):'';


                    var soundRecording;
                    if (talkingData && talkingData.hasOwnProperty('LinkedChannel')){
                        soundRecording = talkingData.LinkedChannel.Data.MonitorFilename;
                    }else if (hungupData && hungupData.hasOwnProperty('Data')){
                        soundRecording = hungupData.Data.MonitorFilename;
                    }else {
                        soundRecording = '';
                    }

                    params.begintime = begintime;
                    params.endtime = endTime;
                    params.callRingEndTime = callRingEndTime;
                    params.soundRecording = soundRecording;
                    callService.submitRing(params)
                        .success(function(data){
                            deferred.resolve(data);
                        })
                        .error(function(XMLHttpRequest){
                            console.log(XMLHttpRequest);
                            deferred.reject(XMLHttpRequest);
                        });
                }else {
                    sysService.showMessage('咨询类型或咨询信息必填！');
                }
                return deferred.promise;
            }
        }
    })
    .factory('ShowAddClientModal',function ($modal) {
        var modalInstance;
        return{
            showAddClientModal : function (data) {
                 modalInstance = $modal.open({
                    templateUrl: 'pukang-callportal/client/addClient/addClient.html',
                    controller: 'AddClientController',
                    keyboard:false,
                    resolve:{
                        commonData:function () {
                            return data;
                        }
                    }
                });
                return modalInstance;
            }
        }
    })
    .factory('ShowUpdateClientModal',function ($modal) {
        var modalInstance;
        return{
            showUpdateClientModal : function (data) {
                modalInstance = $modal.open({
                    templateUrl: 'pukang-callportal/client/updateClient/updateClient.html',
                    controller: 'UpdateClientController',
                    keyboard:false,
                    resolve:{
                        commonData:function () {
                            return data;
                        }
                    }
                });
                return modalInstance;
            }
        }
    });