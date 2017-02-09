/** ----------------  GastoController   ---------------- */
var app = angular.module('GastoControllerMdl',['ConstantsServiceMdl']);

app.controller('gastoController',['$scope', '$http', 'PATH_APP', 'gastoService',
                                  'orcamentoService','dateService','logService','$window',
                                  function($scope, $http, PATH_APP, gastoService,
                                		  orcamentoService,dateService, logService,$window) {
	
	console.log('Criando gasto controller');
	$scope.init =  function () {
		try {
			console.log('Criando gasto controller');
			var infOrcamento = orcamentoService.getIdDescricaoOrcamento();
			$scope.descOrcamento = infOrcamento.descOrcamento;
			$scope.idOrcamento = infOrcamento.idOrcamento
			$scope.gastos =	   orcamentoService.getListaGastos();
			$scope.data = dateService.getDataFormatada();
		} catch (err) {
			console.log('Erro ao iniciar gasto: ' + err.message);
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

			gastoService.paga($scope.gastoSel,$scope.data, $scope.valor, $scope.idOrcamento, 
				$scope.descricao).then(
					function(response) {
						$scope.mensagem=response.mensagem;
						logService.loga('Resultado ' + response.tipoMensagem+ ' '+ response.mensagem);
						if (response.tipoMensagem=='OK') {
							$scope.limpa();
							$scope.mensagemInfo=response.mensagem;
							orcamentoService.carregaResumo(response.resumo);
						} else {
							$scope.mensagemInfo='';
							$scope.mensagemErro=response.mensagem;
						}
					},
					function(error) {
						logService.loga('Erro ao pagar ' + error);
						$scope.mensagemInfo='';
						$scope.mensagemErro=response.mensagem;
					}
				);
		}}

	$scope.limpa = function() {
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