package net.pdp7.f1.prediction.scraper;

import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;

public class CircuitNameNormalizerTest extends TestCase {

	public void test() throws Exception {
		CircuitNameNormalizer normalizer = new CircuitNameNormalizer(new ClassPathResource("/net/pdp7/f1/prediction/scraper/circuit_synonyms.txt"));
		
		assertEquals("Valencia Street Circuit, Valencia", normalizer.normalize("Valencia Street Circuit, Valencia†"));
		assertEquals("Circuit de Monaco, Monte Carlo", normalizer.normalize("Circuit de Monaco, Monte-Carlo"));
	}
	
}
