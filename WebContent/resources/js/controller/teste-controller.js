var testeApp = angular.module('testeApp',['TesteServiceMdl']);

testeApp.controller('testeController',['$http','$scope','testeService', function($http, $scope,testeService){
	
	$scope.doGet = function doPost() {
		console.log('Testando ...');
	//	testeService.doGet().
	//		success(function(data, status, headers, config ){
	//			console.log(data);
	//			console.log(headers);
	//		}).
	//		error(function(data, status, headers, config){
	//			console.log(data);
	//		});
		testeService.doPost();
	}
		
}]);