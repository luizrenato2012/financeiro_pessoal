var modulo = angular.module('OrcamentoControllerMdl', ['OrcamentoServiceMdl']);

modulo.controller('orcamentoController', ['$scope','orcamentoService','$window', function($scope, orcamentoService, $window){
	
	$scope.orcamentoAtivo = {};
	$scope.mensagem="";
	
	$scope.lista = function() {
		orcamentoService.listaOrcamento().
			success(function(data, status, headers, config){
				//console.log(data);
				$scope.listaOrcamento = data.orcamentos;
			}).
			error(function(data, status, headers, config){
				var msg = 'Erro ' + data.mensagem;
				console.log(msg);
				$scope.mensagem=msg;
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
		}
	}
	
	$scope.gravaAtivacao = function() {
		if ($scope.orcamentoAtivo!=null){
			console.log('Gravando orcamento ativo '+ $scope.orcamentoAtivo);
			orcamentoService.ativaOrcamento($scope.orcamentoAtivo.id)
				.success(function(data, status, headers, config){
					if (data.tipoMensagem=='OK'){
						$window.location.href="#!/redirect";
					}else {
						$scope.mensagem="Não foi possivel ativar";
						console.log(data);
					}
				})
				.error(function(data, status, headers, config){
					$scope.mensagem="Erro ao ativar";
					console.log(data);
				})
		}
	}
	
}]);