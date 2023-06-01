package namedEntity.heuristic;

import java.util.ArrayList;
import java.util.List;

import namedEntity.heuristic.Stanford.Pipeline;

// import org.apache.spark.sql.connector.catalog.functions.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StandfordHeuristic extends Heuristic {

	public boolean isEntity(String word) {
		return true;
	}

	public List<String> getEntities(String text) {

		StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
		CoreDocument coreDocument = new CoreDocument(text);

		stanfordCoreNLP.annotate(coreDocument);

		List<CoreSentence> sentences = coreDocument.sentences();
		List<String> entities = new ArrayList<String>();
		
		for (CoreSentence sentence : sentences) {
			entities.addAll(0, sentence.nerTags());
		}

		return entities;
	}

	public static void main(String[] args) {
		// StandfordHeuristic sh = new StandfordHeuristic();
	}

}
