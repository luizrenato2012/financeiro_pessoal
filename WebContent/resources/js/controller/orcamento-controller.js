var modulo = angular.module('OrcamentoControllerMdl', ['OrcamentoServiceMdl']);

modulo.controller('orcamentoController', ['$scope','orcamentoService', function($scope, orcamentoService){
	
	$scope.orcamentoAtivo = {};
	
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
	
	$scope.teste = function(id) {
		console.log('Testando orcamento: '+ id);
	}
	
	$scope.selecionaAtivo = function(orcamentoIn) {
		console.log('selecionado '+ orcamentoIn);
		$scope.orcamentoAtivo = orcamentoIn;
		$scope.listaTemp = [];
		var id = $scope.orcamentoAtivo.id;
		var orcamento = {};
		
		for( var i = 0 ; i < $scope.listaOrcamento.length; i++) {
			orcamento = $scope.listaOrcamento[i];
			if (orcamento.id!=id) {
				console.log('desativando orcamento');
				orcamento.ativo=false;
			}
		//	$scope.listaTemp.push(orcamento);
		}
	//	$scope.listaOrcamento = $scope.listaTemp;
	}
	
	$scope.gravaAtivacao = function() {
		if ($scope.orcamentoAtivo!=null){
			console.log('Gravando orcamento ativo '+ $scope.orcamentoAtivo);
		}
	}
	
}]);