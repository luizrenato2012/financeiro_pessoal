var app  =angular.module('GastoServiceMdl',['ServiceUtilMdl','ConstantsServiceMdl']);

app.service('gastoService', ['$http', 'PATH_APP' , '$q','logService','$cacheFactory',
                             function($http, PATH_APP, $q, logService, $cacheFactory) {
//	logService.loga('criando gastoService ');
	var cache = $cacheFactory('cacheGasto');
	
	
	this.init = function() {
		//resumoService.carregaResumo();
		//this.carregaGastos();
	}
	
	this.listaPagamento = function(dataInicial, dataFinal, tipoPagamento) {
		var params = {
			acao: 'listaPagamento',
			dataInicial: dataInicial,
			dataFinal: dataFinal,
			tipo: tipoPagamento
		};
		return $http.get(PATH_APP + 'orcamento', {params: params});
	}

	this.paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		
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
	
	this.init();
	
}]);