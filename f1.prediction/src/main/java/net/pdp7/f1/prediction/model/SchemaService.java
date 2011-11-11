package net.pdp7.f1.prediction.model;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

public class SchemaService {

	protected final SimpleJdbcTemplate jdbcTemplate;

	public SchemaService(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void createSchema() {
		SimpleJdbcTestUtils.executeSqlScript(jdbcTemplate, new ClassPathResource("/net/pdp7/f1/prediction/model/schema.sql"), false);
	}
	
}
