/**
 * This class could be the handler for an AWS Lambda function powering an Alexa Skills Kit
 * experience. To do this, simply set the handler field in the AWS Lambda console to
 * "integratedHomeProject.IntegratedHomeSpeechletRequestStreamHandler" For this to work, you'll also need to build
 * this project using the lambda-compile Ant task and upload the resulting zip file to power
 * your function.
 */
package integratedHomeProject;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * 
 * @author KarimHalani
 * @version 1.0
 */
public class IntegratedHomeSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.98f38295-8b59-47a0-890c-37e780db8295");
    }

    public IntegratedHomeSpeechletRequestStreamHandler() {
        super(new IntegratedHomeSpeechlet(), supportedApplicationIds);
    }
}
