var testeApp = angular.module('testeApp',['TesteServiceMdl']);

testeApp.controller('testeController',['$scope', function($scope){
	
	$scope.doGet = function doPost() {
		console.log('Testando ...');
	}
		
}]);