package app.metrics.boot.com.aol.micro.server;


import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.aol.micro.server.boot.config.Microboot;
import com.aol.micro.server.boot.config.MicrobootApp;
import com.aol.micro.server.spring.metrics.CodahaleMetricsConfigurer;
import com.aol.micro.server.testing.RestAgent;

@Microboot
public class MetricsRunnerTest {

	RestAgent rest = new RestAgent();
	
	MicrobootApp server;
	@Before
	public void startServer(){
		CodahaleMetricsConfigurer.setInit( metricRegistry -> 		 TestReporter
		         .forRegistry(metricRegistry)
		         .build()
		         .start(10, TimeUnit.MILLISECONDS));
		
		server = new MicrobootApp(  ()-> "simple-app");
		server.start();

	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
	@Test
	public void runAppAndBasicTest() throws InterruptedException, ExecutionException, IOException{
		
		
		
		
		
		assertThat(rest.get("http://localhost:8080/simple-app/metrics/ping"),is("ok"));
		
		
		assertThat(TestReporter.getTimer().size(),greaterThan(0));
	}
	
	
	
}
