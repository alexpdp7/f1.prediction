package net.pdp7.f1.prediction.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class ModelUtils {
	
	protected ModelUtils() {}
	
	public static String get20052011DatabaseUrl() throws IOException {
		File tempFile = File.createTempFile("npfpmmtu", ".h2.db");
		
		FileOutputStream out = new FileOutputStream(tempFile);
		IOUtils.copy(ModelUtils.class.getResourceAsStream("/net/pdp7/f1/prediction/model/resultsdb2005-2011.h2.db"), out);
		out.close();
		
		return "jdbc:h2:" + tempFile.getAbsolutePath().replace(".h2.db", "");
	}
	
	public static DataSource get20052011DataSource() throws IOException {
		return new SingleConnectionDataSource(get20052011DatabaseUrl(), true);
	}
}
