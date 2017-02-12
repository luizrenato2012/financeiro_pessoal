var app = angular.module('TesteServiceMdl',[]);

app.service('testeService',['$http', function($http){
	
	return {
		doPost: function() {
			console.log('DoPost');
		},
		
		doGet: function() {
			console.log('doGet');
			var url = 'http://localhost:8080/financeiro_pessoal/orcamento';
			var params = {acao: 'teste'};
			return $http.get(url,{params: params} );
		}
	};
	
}]);