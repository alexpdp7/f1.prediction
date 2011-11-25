package net.pdp7.f1.prediction.scraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.core.io.Resource;

public class CircuitNameNormalizer {

	protected final Map<String, String> synonyms = new HashMap<String, String>();
	
	public CircuitNameNormalizer(Resource synonymsFile) {
		try {
			LineIterator lineIterator = IOUtils.lineIterator(synonymsFile.getInputStream(), "UTF-8");
			
			String canonicalName = null;
			
			while(lineIterator.hasNext()) {
				String line = lineIterator.next();
				
				if(line.startsWith("\t")) {
					synonyms.put(line.replace("\t", ""), canonicalName);
				}
				else {
					canonicalName = line;
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String normalize(String circuitName) {
		String clean = circuitName.replaceAll("†", "");
		return synonyms.containsKey(clean) ? synonyms.get(clean) : clean;
	}
}
