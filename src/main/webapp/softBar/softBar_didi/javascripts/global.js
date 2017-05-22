
var hollyglobal = {
    hangupEvent: function (peer,callback) {
        console.log(peer);
        console.dir(peer);
        callback(peer);  //回调函数，位于softphonebar.js 1747行

    },
    ringEvent: function (peer,callback) {
        hollyglobal.ringFlag = true;
		var callSheetId = peer.Data.CallSheetID;
		var Agent = peer.Data.Agent;
		var callerCity = peer.CallerCity;
		var CallerCityCode = peer.CallerCityCode;
		var CallerProvince = peer.CallerProvince;
		var CallerProvinceCode = peer.CallerProvinceCode;
		var RingTime = peer.Data.RingTime;
		var ExtenType = peer.ExtenType;
		var FromCid  = peer.FromCid;
		var FromDid = peer.FromDid;
		var UserID = peer.UserID;
        console.log(peer);
		console.dir(peer);
        callback(peer);  //实现函数在 softphonebar.js 1728行
    },
    talkingEvent: function (peer,callback) {
        console.dir(peer);
        console.log(peer);
        callback(peer);
    },
    loginSuccessCallback: null,

    loginFailureCallback: function (peer) {
        console.log(peer);
    },
    pbxs: [
        {
            PBX: '2.3.1.101',
            pbxNick: '101',
            NickName: '101',
            proxyUrl: "http://10.8.15.222"
        }
    ],
    accountId: 'N000000008355',
    sipConfigId: '2.3.1.101',
    monitorPassword: '7pu3refwds98172e',
    monitorUserID: '2387rufhinjvcx73rfws',
    loginBusyType: "0",
    loginExtenType: "Local",
    loginPassword: "",
    loginUser: "",
    loginProxyUrl: "http://210.14.88.129",
    isDisplayInvestigate: true,
    isDisplayConsult: true,
    isHiddenNumber: false,
    isMonitorPage: false,
    isDisplayTransfer: true
};