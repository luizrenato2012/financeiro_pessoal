var UtilService  = angular.module('ServiceUtilMdl',[]);

UtilService.service('dateService', function() {
	
	this.getDataFormatada = function() {
		var arData = new Date();
		var intDia = arData.getDate();
		var intMes = arData.getMonth()+1;
		var strAno = arData.getUTCFullYear().toString();
		
		var strDia = intDia.toString();
		var strMes = intMes.toString();
		
		if (strDia.length==1) {
			strDia='0'+strDia;
		}
		
		if (strMes.length==1) {
			strMes='0'+strMes;
		}
		return strDia+"/"+strMes+"/"+strAno;
	}
	
	this.getDataHoraFormatada = function() {
		return this.getDataFormatada() + ' '+  this.getHoraFormatada();
	}
	
	this.getHoraFormatada = function() {
		var hora = new Date();
		return 	this.completaEsquerda(hora.getHours())+':' + 
				this.completaEsquerda(hora.getMinutes())+':' +
				this.completaEsquerda(hora.getSeconds());
	}
	
	this.completaEsquerda = function(valor) {
		var str = valor.toString();
		if (str.length==1) {
			str='0'+str;
		}
		return str;
	}
});

UtilService.service('logService', ['dateService',function(dateService) {
	
	this.loga = function(str) {
		console.log (dateService.getHoraFormatada() + " -> " + str);
	}
}]);

/** interceptor p/ log*/
UtilService.factory('ConsoleInterceptor', function() {
	var interceptor = {
			'request' : function(config) {
				//console.log('request :' + angular.toJson(config));
				return config;
			},
			'response' : function(response) {
			//	console.log('response: ' +angular.toJson(response));
				return response;
			},
			'requestError' : function(rejection) {
				console.log('requestError: ' +angular.toJson(rejection));
				return rejection;
			},
			'responseError' : function(rejection) {
				console.log('responseError: ' +angular.toJson(rejection));
				return rejection;
			}
	};
	return interceptor;
});

UtilService.config(['$httpProvider', function($httpProvider){
	//adicionar interceptor no array
	$httpProvider.interceptors.push('ConsoleInterceptor');
}]);