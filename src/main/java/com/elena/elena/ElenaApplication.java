package com.elena.elena;

import com.elena.elena.util.ElenaUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class ElenaApplication {

	@Value("${sqlite.db}") String sqliteConnectionString;



	public static void main(String[] args) {
		SpringApplication.run(ElenaApplication.class, args);
	}

	@Bean
	public DataSource sqliteDataSource(){
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setJdbcUrl(sqliteConnectionString);
		comboPooledDataSource.setMinPoolSize(5);
		comboPooledDataSource.setAcquireIncrement(5);
		comboPooledDataSource.setMaxPoolSize(20);

		return comboPooledDataSource;
	}

}
