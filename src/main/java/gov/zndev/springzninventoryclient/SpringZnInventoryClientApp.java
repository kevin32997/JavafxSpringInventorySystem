package gov.zndev.springzninventoryclient;

import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringZnInventoryClientApp {

	public static void main(String[] args) {

		//SpringApplication.run(SpringZnInventoryClientApp.class, args);

		Application.launch(FXApplication.class);

	}

	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
		// Would also work with javafx-weaver-core only:
		// return new FxWeaver(applicationContext::getBean, applicationContext::close);
		return new SpringFxWeaver(applicationContext); //(2)
	}

}
