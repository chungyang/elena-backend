package com.elena.elena;

import com.elena.elena.util.ElenaUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootApplication
public class ElenaApplication {



	public static void main(String[] args) {
		SpringApplication.run(ElenaApplication.class, args);
	}

	@Bean
	public DataSource sqliteDataSource(){
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		String sqlitePath = ElenaUtils.getFilePath("cs520.db");
		comboPooledDataSource.setJdbcUrl("jdbc:sqlite:" + sqlitePath);
		comboPooledDataSource.setMinPoolSize(5);
		comboPooledDataSource.setAcquireIncrement(5);
		comboPooledDataSource.setMaxPoolSize(20);

		return comboPooledDataSource;
	}

}
