package org.terrier.asp.test.main;

import org.terrier.asp.applications.ASPProgramScheduler;

public class BasicTest {

	public static void main(String[] args) {
		//String[] args1 = {"configurationTemplates\\programs\\testMongoDBOut.program"};
		String[] args1 = {"configurationTemplates\\programs\\trecis.classifier.program"};
		
		ASPProgramScheduler.main(args1);
	}
	
}
