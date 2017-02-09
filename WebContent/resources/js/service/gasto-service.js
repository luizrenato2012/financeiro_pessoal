var app  =angular.module('GastoServiceMdl',['UtilMdl']);

app.service('gastoService', ['$http', 'PATH_APP', 'APPLICATION_JSON' , '$q','logService','$cacheFactory',
                             function($http, PATH_APP, APPLICATION_JSON, $q, logService, $cacheFactory) {
	logService.loga('criando gastoService ');
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
//			headers: {'Content-type' : APPLICATION_JSON}
//		}).success (function(data,status,headers,config,params) {
//			defer.resolve(data);
//		}).error(function(data,status,headers,config,params) {
//			defer.reject(data);
//		});
		//	logService.loga('Retornando inicio orcamento');
//		return defer.promise;
//	}

	this.listaPagamento = function(dataInicial, dataFinal, tipoPagamento) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=listaPagamento&dataInicial='+ dataInicial +
			'&dataFinal='+ dataFinal + '&tipo='+ tipoPagamento,
			headers: {'Content-type': APPLICATION_JSON}	
		}).success(function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}

	this.paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=pagaGasto&id='
			+ gastoSel.value+"&data="+ data + 
			'&valor='+valor+"&idOrcamento="+idOrcamento +
			'&descricao='+descricao,
			headers: {'Content-type' : APPLICATION_JSON}
		}).success (function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}
	
//	this.buscaGastos = function () {
//		var defer = $q.defer();
//		$http({
//			method: 'GET',
//			url: PATH_APP + 'orcamento?acao=listaGasto'
			//	headers: {'Content-type' : APPLICATION_JSON}
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