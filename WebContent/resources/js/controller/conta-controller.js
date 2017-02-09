
/** ---------------------- ContaController ---------------------- */
var app = angular.module('ContaControllerMdl',[]);

app.controller('contaController',['$scope', '$http', 'PATH_APP', 'APPLICATION_JSON','gastoService',
                                  'orcamentoService', 'dateService','logService','$window',
                                  function($scope, $http, PATH_APP, APPLICATION_JSON, gastoService,
                                		  orcamentoService,dateService, logService,$window) {
	
	//console.log('Criando conta controller');
	$scope.init =  function () {
		try {
		//	console.log('Criando gasto controller');
			var infOrcamento = orcamentoService.getIdDescricaoOrcamento();
			$scope.descOrcamento = infOrcamento.descOrcamento;
			$scope.idOrcamento = infOrcamento.idOrcamento
			$scope.contas =	   orcamentoService.getListaContas($scope.idOrcamento);
			$scope.data = dateService.getDataFormatada();
		} catch (err) {
			logService.loga('Erro ao iniciar conta: ' + err.message);
		}
	}
	//executa init() ao criar o controller 
	$scope.init();

	$scope.paga = function() {
		$scope.mensagemInfo='Enviando...';
		$scope.mensagemErro='';
		var resValidacao = $scope.validaCampos($scope.idOrcamento, $scope.contaSel, $scope.data, $scope.valor);
		if (resValidacao!='') {
			logService.loga('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {
			contaService.paga($scope.gastoSel,$scope.data, $scope.valor, $scope.idOrcamento, 
				$scope.descricao).then(
					function(response) {
						$scope.mensagem=response.mensagem;
						logService.loga('Resultado paga conta:' + response.tipoMensagem+ ' '+ response.mensagem);
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

	$scope.validaCampos = function(idOrcamento, contaSel, data, valor) {
		var msg='';
		if (idOrcamento== undefined || idOrcamento==null || idOrcamento==0) {
			msg='Orçamento invalido';
		} else if (contaSel== undefined || contaSel=='') {
			msg='Selecione um gasto';
		} else if (data== undefined || data=='') {
			msg='Digite a data';
		} else if (valor== undefined || valor=='' || valor== 0) {
			msg='Digite um valor';
		}
		return msg;
	}

	//redireciona p/ pagina de login
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}


}]);