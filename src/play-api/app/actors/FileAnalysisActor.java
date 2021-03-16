package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.google.common.collect.Lists;
import messages.FileAnalysisMessage;
import messages.FileProcessedMessage;
import messages.LineMessage;
import models.LineProcessingResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Akka High level actor
 * <p>
 * This actor will be in charge of creating other actors and send them messages to coordinate the work.
 * It also receives the results and prints them once the processing is finished.
 */
public class FileAnalysisActor extends UntypedAbstractActor {

    private final List<LineProcessingResult> hMap = new ArrayList<>();
    private long fileLineCount;
    private long processedCount;
    private ActorRef analyticsSender = null;

    /**
     * @param message -
     * This actor can receive two different messages, FileAnalysisMessage or LineProcessingResult
     * any other type will be discarded using the unhandled method
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof FileAnalysisMessage) {

            List<String> lines = FileUtils.readLines(new File(((FileAnalysisMessage) message).getFileName()), "UTF-8");

            fileLineCount = lines.size();
            processedCount = 0;

            // Stores a reference to the original sender to send back the results later on
            analyticsSender = this.getSender();

            List<List<String>> listsLines = Lists.partition(lines, Integer.parseInt(System.getenv("NUMBER_OF_LINES")));

            for (List<String> l : listsLines) {

                Props props = Props.create(LineProcessor.class);
                ActorRef lineProcessorActor = this.getContext().actorOf(props);

                // sends a message to the new actor with the line payload
                lineProcessorActor.tell(new LineMessage(l), this.getSelf());
            }

            //  for (String line : lines) {
            // creates a new actor per each line of the  file
            //     Props props = Props.create(LineProcessor.class);
            //     ActorRef lineProcessorActor = this.getContext().actorOf(props);
            //
            // sends a message to the new actor with the line payload
            //      lineProcessorActor.tell(new LineMessage(Collections.singletonList(line)), this.getSelf());
            //  }

        } else if (message instanceof LineProcessingResult) {

            // a result message is received after a LineProcessor actor has finished processing a line
            hMap.add(((LineProcessingResult) message));

            // if the file has been processed entirely, send a termination message to the main actor
            processedCount = processedCount + ((LineProcessingResult) message).getTweets().size();

            if (fileLineCount == processedCount) {
                // send done message
                analyticsSender.tell(new FileProcessedMessage(hMap), ActorRef.noSender());
            }

        } else {
            // Ignore message
            this.unhandled(message);
        }


    }


}
