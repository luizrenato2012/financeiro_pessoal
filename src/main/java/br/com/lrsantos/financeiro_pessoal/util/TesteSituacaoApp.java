package br.com.lrsantos.financeiro_pessoal.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.lrsantos.financeiro_pessoal.model.bean.SituacaoDespesa;

public class TesteSituacaoApp {
	
	public static void main(String[] args) {
		Ocorrencia o1 = new Ocorrencia("", new Date() , SituacaoDespesa.PAGA);
		Ocorrencia o2 = new Ocorrencia("", new Date() , SituacaoDespesa.PAGA);
		Ocorrencia o3 = new Ocorrencia("", new Date() , SituacaoDespesa.PAGA);
		System.out.println(o1.getSituacao().getName());
		System.out.println(o2.getSituacao().getName());
		System.out.println(o3.getSituacao().getName());
		ExecutorService pool = Executors.newSingleThreadExecutor();
		//pool.submit(task)
	}

}
