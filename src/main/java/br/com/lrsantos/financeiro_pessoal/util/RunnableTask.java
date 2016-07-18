package br.com.lrsantos.financeiro_pessoal.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunnableTask implements Runnable{

	@Override
	public void run() {
		try {
			System.out.println("Iniciando execu��o");
			Thread.sleep(3000);
			System.out.println("Terminada execucao");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}



	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> res = executor.submit(new CallableTask());
		while (!res.isDone()) {
			System.out.println("Aguardando... " );
		}
		System.out.println("Finalizando "+ res.get());
		executor.shutdown();

	}

}

class CallableTask implements Callable<String> {


	@Override
	public String call() throws Exception {
		System.out.println("Iniciando CallableTask");
		Thread.sleep(3000);
		System.out.println("Terminado CallableTask");
		return "Final";
	}

}
