
/** ---------------------- ContaController ---------------------- */
var app = angular.module('ContaControllerMdl',['ConstantsServiceMdl','ContaServiceMdl']);

app.controller('contaController',['$scope', 'contaService','orcamentoService', 'dateService','logService','$window',
                                  function($scope, contaService, orcamentoService,dateService, logService,$window) {
	
	//console.log('Criando conta controller');
	$scope.contaSel={};
	$scope.contaSel.valor=0;
	
	$scope.init =  function () {
		try {
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
		var resValidacao = $scope.validaCampos($scope.idOrcamento, $scope.contaSel, $scope.data, $scope.contaSel.valor);
		
		if (resValidacao!='') {
			logService.loga('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {
			contaService.paga($scope.contaSel,$scope.data, $scope.contaSel.valor, $scope.idOrcamento).
				success (function(data,status,headers,config,params) {
					$scope.mensagem=data.mensagem;
					logService.loga('Resultado paga conta:' + data.tipoMensagem+ ' '+ data.mensagem);
					if (data.tipoMensagem=='OK') {
						$scope.limpa();
						$scope.mensagemInfo=data.mensagem;
						orcamentoService.atualizaResumoConta(data.orcamento.resumo, data.orcamento.contas);
					} else {
						$scope.mensagemInfo='';
						$scope.mensagemErro=data.mensagem;
					}}).
				error(function(data,status,headers,config,params) {
					logService.loga('Erro ao pagar ' + data);
					$scope.mensagemInfo='';
					$scope.mensagemErro=data.mensagem;
				});
		}
	}

	$scope.limpa = function() {
		$scope.data='';
		//$scope.contaSel.valor='';
		$scope.contaSel = {};
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