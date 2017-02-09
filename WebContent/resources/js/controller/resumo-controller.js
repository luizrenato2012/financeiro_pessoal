var app = angulajs.module('ResumoControllerMdl', ['UtilMdl']);

app.controller('resumoController',['$scope','orcamentoService','logService','$window', 
                                   function($scope,orcamentoService, logService, $window) {

	//logService.loga('Criando resumeController');

	$scope.resumeOrcamento = function() {
		var resumo = orcamentoService.getResumoOrcamento();

		$scope.valorDisponivel = resumo.valorDisponivel;
		$scope.valorPendente   = resumo.valorPendente;
		$scope.valorSobrante   = resumo.valorSobrante;
		$scope.descOrcamento   = resumo.descOrcamento;
		$scope.idOrcamento   = resumo.idOrcamento;
		$scope.contaPendente   = resumo.contaPendente;
		$scope.gastoPendente   = resumo.gastoPendente;

//		);

	}

	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}

	$scope.resumeOrcamento();
}]);
