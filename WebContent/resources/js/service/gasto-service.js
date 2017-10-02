var app  =angular.module('GastoServiceMdl',['ServiceUtilMdl','ConstantsServiceMdl']);

app.factory('gastoService', function($http, PATH_APP, $q, logService) {
	
	var _listaPagamento = function(dataInicial, dataFinal, tipoPagamento) {
		var params = {
		acao: 'listaPagamento',
		dataInicial: dataInicial,
		dataFinal: dataFinal,
		tipo: tipoPagamento
		};
		return $http.get(PATH_APP + 'orcamento', {params: params});
	}
	
	var _paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		var params =  {
				acao: 'pagaGasto',
				id : gastoSel.id,
				data: data,
				valor: valor,
				idOrcamento: idOrcamento ,
				descricao: descricao
		};
		return $http.put(PATH_APP + 'orcamento',{}, {params:params});
	}
	
	return {
		listaPagamento : _listaPagamento,
		paga : _paga
	}
	
	
});