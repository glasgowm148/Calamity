package org.terrier.asp.test.filebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.terrier.asp.conf.ASPReaderConf;

public class MakeReaderConf {

public static void main(String[] args) {
		
		String dir = "/local/tr.kba/TRECIS/ASP/TestConf/";
		
		Map<String,String> variables = new HashMap<String,String>();
		variables.put("inputFile", "/local/tr.kba/SUPER/Datasets/Worlds2016/#Worlds2016.1.json.gz");
		
		List<String> guaranteedOutputFrameTypes = new ArrayList<String>();
		guaranteedOutputFrameTypes.add("KeyFrame");
		guaranteedOutputFrameTypes.add("TextFrame");
		guaranteedOutputFrameTypes.add("MetaDataFrame");
		
		ASPReaderConf conf1 = new ASPReaderConf("JSONTweetStream",
				"org.terrier.asp.recievers.JSONTweetFileReciever",
				variables,
				guaranteedOutputFrameTypes);
		
		try {
			conf1.save(dir+"JSONTweetStream.reader.conf");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
