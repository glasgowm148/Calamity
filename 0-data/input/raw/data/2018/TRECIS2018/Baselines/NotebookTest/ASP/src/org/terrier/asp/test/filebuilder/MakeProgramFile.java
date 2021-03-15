package org.terrier.asp.test.filebuilder;

import java.util.ArrayList;
import java.util.List;

import org.terrier.asp.conf.ASPProgram;
import org.terrier.asp.conf.ASPUnit;
import org.terrier.asp.conf.ASPUnitType;

public class MakeProgramFile {

	public static void main(String[] args) {
		
		String dir = "/local/tr.kba/TRECIS/ASP/TestConf/";
		String workingDIR = "/local/tr.kba/TRECIS/ASP/TestWorking/";
		
		List<String> recievers = new ArrayList<String>();
		recievers.add("JSONTweetStream");
		
		List<ASPUnit> units = new ArrayList<ASPUnit>();
		
		List<String> unit1M = new ArrayList<String>();
		unit1M.add("NullModule");
		List<String> unit1C = new ArrayList<String>();
		unit1C.add(">");
		/*ASPUnit unit1 = new ASPUnit(ASPUnitType.stream, unit1M, unit1C);
		
		units.add(unit1);
		
		ASPProgram program = new ASPProgram(workingDIR, new ArrayList<String>(), recievers, units);
		try {
			program.save(dir+"example.program");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
}
