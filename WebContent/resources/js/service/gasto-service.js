var app  =angular.module('GastoServiceMdl',['ServiceUtilMdl','ConstantsServiceMdl']);

app.service('gastoService', ['$http', 'PATH_APP' , '$q','logService','$cacheFactory',
                             function($http, PATH_APP, $q, logService, $cacheFactory) {
//	logService.loga('criando gastoService ');
	var cache = $cacheFactory('cacheGasto');
	
	
	this.init = function() {
		//resumoService.carregaResumo();
		//this.carregaGastos();
	}
	
//	this.carregaGastos = function() {
//		logService.loga('Carregando gastos');
//		this.buscaGastos().then(
//			function(response) {
//				cache.put('listaGastos',response.gastos);
///			}, 
//			function(error) {
//				console.error(error);
//			}
//		);
//	}
	
//	this.iniciaOrcamento = function() {
//		var defer = $q.defer();
//		$http({
//			method: 'GET',
//			url: PATH_APP + 'orcamento?acao=iniciaOrcamento',
//			headers: {'Content-type' : application/json}
//		}).success (function(data,status,headers,config,params) {
//			defer.resolve(data);
//		}).error(function(data,status,headers,config,params) {
//			defer.reject(data);
//		});
		//	logService.loga('Retornando inicio orcamento');
//		return defer.promise;
//	}

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
		return $http.get(PATH_APP + 'orcamento', {params:params});

	}
	
//	this.buscaGastos = function () {
//		var defer = $q.defer();
//		$http({
//			method: 'GET',
//			url: PATH_APP + 'orcamento?acao=listaGasto'
			//	headers: {'Content-type' : application/json}
//		}).success(function(data,status,headers,config,params){
			//console.log(data.gastos);
//			defer.resolve(data);
//		}).error(function(data,status,headers,config,params) {
//			console.error('Erro ao buscar gastos ' + data);
//			defer.reject(data);
//		});
//		return defer.promise;
//	};
	
//	this.getListaGastos = function() {
//		if (cache.get('listaGastos') == null || cache.get('listaGastos') == undefined) {
//			return "";
//		}
//		return cache.get('listaGastos');
//	}
	
	this.init();
	
}]);