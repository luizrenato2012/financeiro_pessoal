var modulo = angular.module('OrcamentoControllerMdl', ['OrcamentoServiceMdl']);

modulo.controller('orcamentoController', ['$scope','orcamentoService', function($scope, orcamentoService){
	
	$scope.lista = function() {
		orcamentoService.listaOrcamento().
			success(function(data, status, headers, config){
				console.log(data);
				$scope.listaOrcamento = data.orcamentos;
			}).
			error(function(data, status, headers, config){
				console.log('Erro ' + data.mensagem);
			});
	}
	
	$scope.lista();
	
	$scope.selecionaAtivo = function() {
		var orcamento ={};
		for(orcamento in $scope.listaOrcamento) {
			
		}
	}
	
}]);