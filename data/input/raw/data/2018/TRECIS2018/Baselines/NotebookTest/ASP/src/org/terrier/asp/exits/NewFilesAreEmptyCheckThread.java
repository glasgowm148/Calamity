package org.terrier.asp.exits;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.terrier.asp.conf.ASPProgram;

/**
 * This is an Exit Check Thread that periodically looks to see if
 * the files that are being written by ASP contain any content, if
 * not, then exit will be called.
 * 
 * This assumes we are writing plain text files with:
 *    outputStream.dstream().saveAsTextFiles()
 * @author richardm
 *
 */
public class NewFilesAreEmptyCheckThread extends ExitCheckThread {

	public NewFilesAreEmptyCheckThread(ASPProgram currentProgram,
			JavaStreamingContext ssc) {
		super(currentProgram, ssc);
	}

	@Override
	public void run() {
		
		// track the list of folders in our working directory that we checked
		// during past iterations
		Set<String> instanceFoldersToIgnore = new HashSet<String>();
		
		File dir = new File(currentProgram.getWorkingDIR());
		
		while (true) {
			
			// Current list of files 
			String[] folders = dir.list();
			 
			boolean containsNewData = false;
			for (String folder : folders) {
				
				// Skip folders and files from other processes
				if (!folder.endsWith(".ASPOutput")) continue;
				
				String fullFolderName = dir+"/"+folder;
				
				// skip other files that might be in the working directory
				File currentFolder = new File(fullFolderName);
				if (currentFolder.isFile()) continue;
				
				// If we reach here, then the file should be a valid output folder
				// Skip any folders checked from a previous iteration
				if (instanceFoldersToIgnore.contains(fullFolderName)) continue;
				
				// contents of the current folder
				String[] files = currentFolder.list();
				
				for (String sparkFile : files) {
					// skip non-data files
					if (!sparkFile.startsWith("part") || sparkFile.endsWith("crc")) continue;
					
					// check if the file contains data
					try {
						int numLines = countLines(fullFolderName+"/"+sparkFile);
						if (numLines>1) containsNewData=true;
					} catch (Exception e) {}
					
					// Exit early if data found
					if (containsNewData) break;
				}
				
				instanceFoldersToIgnore.add(fullFolderName);

			}
			
			// no new data was found, trigger exit and shutdown
			if (!containsNewData) {
				logger.log(Level.INFO, "   Exit condition checked, no new data produced, triggering exit");
				ssc.stop();
				break;
			}
			
			logger.log(Level.INFO, "   Exit condition checked, new data is still being produced");
			
			// else wait for a bit 
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}

}
