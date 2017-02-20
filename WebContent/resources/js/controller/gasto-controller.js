/** ----------------  GastoController   ---------------- */
var app = angular.module('GastoControllerMdl',['ConstantsServiceMdl','GastoServiceMdl', 'OrcamentoServiceMdl',
                                               	'ServiceUtilMdl']);

app.controller('gastoController',['$scope', '$http', 'PATH_APP', 'gastoService',
                                  'orcamentoService','dateService','logService','$window',
                                  function($scope, $http, PATH_APP, gastoService,
                                		  orcamentoService,dateService, logService,$window) {
	
	$scope.init =  function () {
		try {
			var infOrcamento = orcamentoService.getIdDescricaoOrcamento();
			$scope.descOrcamento = infOrcamento.descOrcamento;
			$scope.idOrcamento = infOrcamento.idOrcamento
			$scope.gastos =	   orcamentoService.getListaGastos();
			$scope.data = dateService.getDataFormatada();
		} catch (err) {
			console.log('Erro ao iniciar gasto: ' + err.message);
			$scope.mensagemInfo='';
			$scope.mensagemErro='Erro ao iniciar tela';
		}
	}
	//executa init() ao criar o controller 
	$scope.init();

	$scope.paga = function() {
		$scope.mensagemInfo='Enviando...';
		$scope.mensagemErro='';
		var resValidacao = $scope.validaCampos();
		if (resValidacao!='') {
			logService.loga('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {
			gastoService.paga($scope.gastoSel,$scope.data, $scope.valor, $scope.idOrcamento,$scope.descricao).
				success(function(data, status, headers, config){
					$scope.mensagem=data.mensagem;
					logService.loga('Resultado ' + data.tipoMensagem+ ' '+ data.mensagem);
					if (data.tipoMensagem=='OK') {
						$scope.limpa();
						$scope.mensagemInfo=data.mensagem;
						//orcamentoService.carregaResumo(data.resumo);
						orcamentoService.atualizaResumoGasto(data.orcamento.resumo, data.orcamento.gastos.listaPendencias);
						$scope.gastos =	orcamentoService.getListaGastos();
					} else {
						$scope.mensagemInfo='';
						$scope.mensagemErro=data.mensagem;
					}
					
				}).
				error(function(data, status, headers, config){
					logService.loga('Erro ao pagar ' + data);
					$scope.mensagemInfo='';
					$scope.mensagemErro=data.mensagem;
				});
		}
	}

	$scope.limpa = function() {
		$scope.gastoSel = {};
		$scope.data='';
		$scope.valor='';
		$scope.descricao='';
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
		$scope.data = dateService.getDataFormatada();
	}

	$scope.limpaMensagens = function() {
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
	}

	$scope.validaCampos = function() {
		var msg='';
		if ($scope.idOrcamento== undefined || $scope.idOrcamento==null || $scope.idOrcamento==0) {
			msg='Orçamento invalido';
		} else if ($scope.gastoSel== undefined || $scope.gastoSel=='') {
			msg='Selecione um gasto';
		} else if ($scope.data== undefined || $scope.data=='') {
			msg='Digite a data';
		} else if ($scope.valor== undefined || $scope.valor=='' || $scope.valor== 0) {
			msg='Digite um valor';
		}
		return msg;
	}

	//redireciona p/ pagina de login
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}
}]);