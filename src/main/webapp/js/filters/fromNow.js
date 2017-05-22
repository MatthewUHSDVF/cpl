'use strict';

/* Filters */
// need load the moment.js to use this filter. 
app
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  })

  .filter('sexFilter',function(){
    return function(sexCode){
        if (sexCode==1){
            return '男';
        }else if(sexCode==2){
            return '女';
        }else{
            return '未知';
        }
    }
})
.filter('statusFilter',function(){
    return function(statusCode){
        if (statusCode==0){
            return '未激活';
        }else if(statusCode==1){
            return '已激活，正常状态';
        }else if(statusCode == 6){
            return '挂失状态'
        }else if(statusCode == 8){
            return '锁定状态'
        }else if(statusCode == 9){
            return '作废';
        }else{
            return '未知';
        }
    }
})
    .filter('finishedFilter',function(){
    return function(status){
        if (status == 0){
            return '未完成';
        }else if(status == 1){
            return '已完成';
        }
    }
})
    .filter('workOrderTypeFilter',function(){
    return function(workorderType){
        if (workorderType == 1){
            return '咨询工单';
        }else if(workorderType == 2){
            return '投诉工单';
        }else if(workorderType == 3){
            return '微信咨询';
        }else if (workorderType == 4){
            return '微信投诉';
        }
    }
})
    .filter('marriedFilter',function(){
    return function(married){
        if (married == 1){
            return '未婚';
        }else if(married == 2){
            return '已婚';
        }else {
            return '未知';
        }
    }
})
    .filter('trustHtml', function ($sce) {

    return function (input) {

        return $sce.trustAsHtml(input);

    }
})
    .filter('subscribeStatusFilter',function () {
        return function (status) {
            if (status == 0){
                return '未确认';
            }else if (status == 1){
                return '已确认';
            }else {
                return '未知';
            }
        }
    })
    .filter('customerTypeFilter',function () {
        return function (status) {
            if (status == 1){
                return '商户';
            }else if (status == 2){
                return '个人';
            }else {
                return '未知';
            }
        }
    })