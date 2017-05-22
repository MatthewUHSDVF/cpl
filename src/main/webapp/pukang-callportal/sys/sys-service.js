/**
 * Created by jason on 16/5/24.
 */
angular.module('app')
    .service('sysService',function($http,$log,$rootScope,$modal,PKO_CONFIG){
        return {
            getProvince: function(param){
                var methodName = 'getProvince';
                console.info(methodName,param);
                if (!param){
                    param = {};
                }
                param.rows = 0;
                return $http({
                    method: 'post',
                    url: PKO_CONFIG.PKCallPortalServerIP.concat('base/queryProvinceByQueryVo'),
                    params: param,
                });
            },
            getCity: function(param){
                var methodName = 'getCity';
                console.info(methodName,param);
                if (!param) {
                    param = {};
                }
                param.rows = 0;
                return $http({
                    method: 'post',
                    url: PKO_CONFIG.PKCallPortalServerIP.concat('base/queryCityByProvinceCode'),
                    params: param,
                });
            },
            getDistrict: function(param){
                var methodName = 'getDistrict';
                console.info(methodName,param);
                if (!param){
                    param = {};
                }
                param.rows = 0;
                return $http({
                    method: 'post',
                    url: PKO_CONFIG.PKCallPortalServerIP.concat('base/queryDistrictByCityCode'),
                    params: param,
                });
            },
            getDictByName: function(dictName){
                if (!dictName) return;
                var methodName = 'getDictByName(),';
                $log.debug(methodName.concat('dictName = '),dictName);
                var param = {dictName: dictName, rows: 0};
                return $http({
                    method: 'post',
                    url: PKO_CONFIG.PKCallPortalServerIP.concat('base/queryDictByName'),
                    params: param
                });
            },
            uploadFile: function(title,url,params){
                var scope = $rootScope.$new();
                scope.url = url;
                scope.title = title;
                scope.params = params;
                return $modal.open({
                    templateUrl: PKO_CONFIG.PKCallPortalServerIP.concat('pukang-callportal/sys/upload-file/upload-file.html'),
                    controller: 'PKUploadFileController',
                    size: 'lg',
                    scope: scope
                });
            },
            downloadFile: function(url,params){
                var temp = document.createElement("form");
                temp.action = url;
                temp.method = "post";
                temp.style.display = "none";
                for (var x in params) {
                    var opt = document.createElement("textarea");
                    opt.name = x;
                    console.info(opt.name);
                    opt.value = params[x];
                    temp.appendChild(opt);
                }
                document.body.appendChild(temp);
                temp.submit();
                return temp;
            },
            showMessage: function (message,title) {
                var scope = $rootScope.$new();
                scope.title = title;
                scope.message = message;
                return $modal.open({
                    templateUrl: PKO_CONFIG.PKCallPortalServerIP.concat('pukang-callportal/sys/show-message/show-message.html'),
                    controller: 'PKShowMessageController',
                    scope: scope
                });
            },
            showConfirm: function (title,message) {
                var scope = $rootScope.$new();
                scope.title = title;
                scope.message = message;
                return $modal.open({
                    templateUrl: PKO_CONFIG.PKCallPortalServerIP.concat('pukang-callportal/sys/show-confirm/show-confirm.html'),
                    controller: 'PKShowConfirmController',
                    scope: scope
                });
            }
        }
    });