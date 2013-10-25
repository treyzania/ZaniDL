package com.treyzania.praseocraft.ftb.downloader.resouces;

import com.treyzania.praseocraft.ftb.downloader.Domain;
import com.treyzania.praseocraft.ftb.downloader.Pcdl;
import com.treyzania.praseocraft.ftb.downloader.jobbing.Job;
import com.treyzania.praseocraft.ftb.downloader.jobbing.Joblist;

public class Worker implements Runnable {

	public final String name;
	public final Domain dom;
	public Joblist list;
	public Thread jobExecutor;

	public Worker(String name, Joblist jl, Domain domainOfOperation) {
		
		this.name = name;
		this.list = jl;
		this.jobExecutor = new Thread(this, "WT_Thread-" + this.name);
		this.dom = domainOfOperation;
		
		Pcdl.log.info("WORKER \'" + this.name + "\'" + "CREATED!");
		
		System.out.println(list);
		
	}
	
	@Override
	public void run() {
		
		Pcdl.log.info("WORKER \'" + this.name + "\'" + " STARTED!");
		
		int failures = 0;
		
		Pcdl.log.info("Job count: " + list.jobs.size());
		
		while (!list.isEmpty()) {
			
			Job j = list.dequeueJob();
			Pcdl.log.finer("WORKER \'" + this.name + "\' EXECUTING JOB: \'" + j.toString() + "\'");
			
			if (j.getDomain() == this.dom) {
				
				boolean success = j.runJob();
				
				synchronized (Pcdl.frame.progressBar) {
					
					int oldValue = Pcdl.frame.progressBar.getValue();
					int newValue = oldValue + 1;
					
					Pcdl.frame.progressBar.setValue(newValue);
					
				}
				
				if (!success) failures++;
				
			}
			
		}
		
		Pcdl.log.info("WORKER \'" + this.name + "\'" + " FINISHED!  FAIL COUNT: " + failures + " FAILURES.");
		
	}
	
}
