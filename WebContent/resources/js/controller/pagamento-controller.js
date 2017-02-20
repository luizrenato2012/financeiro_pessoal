var app = angular.module('PagamentoControllerMdl',['ServiceUtilMdl']); 

app.controller('pagamentoController',['$scope','gastoService','dateService','logService', function($scope, 
		gastoService, dateService, logService) {

	$scope.titulo = 'Lista de Pagamentos';
	$scope.dataInicial=dateService.getDataFormatada();
	$scope.dataFinal=dateService.getDataFormatada();
	$scope.mensagemPesquisaPag='';

	$scope.listaPagamento = function() {
		$scope.mensagemPesquisaPag='Pesquisando...';
		if($scope.tipoPagamento==undefined || $scope.tipoPagamento==null || $scope.tipoPagamento=='Todos') {
			$scope.tipoPagamento='';
		}
		
		gastoService.listaPagamento($scope.dataInicial, $scope.dataFinal, $scope.tipoPagamento)
			.success(function(data,status,headers,config,params) {
				$scope.pagamentos = data.pagamentos;
				$scope.mensagemPesquisaPag='';
			}).error(function(data,status,headers,config,params) {
				logService.loga('Erro ao listar pagamentos ' +error);
				$scope.mensagemErro = error;
				$scope.mensagemPesquisaPag='Erro ao pesquisar';
				console.error(error);	
			});
		
	}

	/** valor total de pagamentos pesquisados */
	$scope.resumePagamentos = function() {
		if($scope.pagamentos !=undefined && $scope.pagamentos!=null) {
			var total = 0.0 ;
			
			var i;
			var pagamento;
			for( i=0; i < $scope.pagamentos.length; i++) {
				pagamento = $scope.pagamentos[i];
				total+= parseFloat(pagamento.valor);
			}
			return total;
		} else {
			return 0.0;
		}
	}

}]);