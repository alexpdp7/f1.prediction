package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import net.pdp7.commons.spring.context.annotation.AnnotationConfigApplicationContextUtils;
import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.model.ModelUtils;
import net.pdp7.f1.prediction.spring.DataSourceConfig;
import net.pdp7.f1.prediction.spring.F1PredictionConfig;
import net.pdp7.f1.prediction.spring.LogConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.UserAbort;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

public class GeneticPredictorEvolver {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext applicationContext = AnnotationConfigApplicationContextUtils.createConfiguredAnnotationConfigApplicationContext(
				MapUtils.createPropertiesFromMap(MapUtils.build("jdbc.url", ModelUtils.get20052011DatabaseUrl()).map), 
				F1PredictionConfig.class, 
				DataSourceConfig.JdbcUrlDataSourceConfig.class,
				GeneticPredictorEvolutionConfig.class,
				LogConfig.class);
		
		@SuppressWarnings("unchecked")
		EvolutionEngine<double[]> evolutionEngine = applicationContext.getBean("evolutionEngine", EvolutionEngine.class);
		EvolutionMonitor<double[]> evolutionMonitor = new EvolutionMonitor<double[]>();
		evolutionEngine.addEvolutionObserver(evolutionMonitor);
		
		evolutionMonitor.showInFrame("Evolution", true);
		
		final UserAbort userAbort = new UserAbort();
		
		JFrame jFrame = new JFrame();
		jFrame.getContentPane().add(new JButton(new AbstractAction("abort") {
			
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				userAbort.abort();
			}
		}));
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
		
		List<EvaluatedCandidate<double[]>> evolvePopulation = evolutionEngine.evolvePopulation(50, 3, userAbort);
		
		Collections.sort(evolvePopulation);
		
		FileWriter fileWriter = new FileWriter("genetic_results.txt");
		BufferedWriter writer = new BufferedWriter(fileWriter);
		
		writer.write("fitness\tdriverPowerRatingDecayRate\tdriverCircuitPowerRatingDecayRate\tteamPowerRatingDecayRate\tteamCircuitPowerRatingDecayRate\tdriverPowerWeight\tdriverCircuitPowerWeight\tteamPowerWeight\tteamCircuitPowerWeight\n");
		
		for(EvaluatedCandidate<double[]> evaluatedCandidate : evolvePopulation) {
			writer.write(
					evaluatedCandidate.getFitness() + "\t" + 
					evaluatedCandidate.getCandidate()[0] + "\t" +
					evaluatedCandidate.getCandidate()[1] + "\t" +
					evaluatedCandidate.getCandidate()[2] + "\t" +
					evaluatedCandidate.getCandidate()[3] + "\t" +
					evaluatedCandidate.getCandidate()[4] + "\t" +
					evaluatedCandidate.getCandidate()[5] + "\t" +
					evaluatedCandidate.getCandidate()[6] + "\t" +
					evaluatedCandidate.getCandidate()[7] + "\n");
		}
		
		writer.flush();
		fileWriter.close();
		
		System.err.println("done!");
		System.exit(0);
	}
}
