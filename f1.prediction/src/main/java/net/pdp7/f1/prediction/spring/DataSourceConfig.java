package net.pdp7.f1.prediction.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public interface DataSourceConfig {

	@Bean
	public DataSource dataSource();

	@Configuration public class JdbcUrlDataSourceConfig implements DataSourceConfig {
		public @Value("${jdbc.url}") String jdbcUrl;
		
		@Bean
		public DataSource dataSource() {
			return new SingleConnectionDataSource(jdbcUrl, true);
		}
	}
}
