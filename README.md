Setup

To run this skill you need to do two things. The first is to deploy the code in lambda, and the second is to configure the Alexa skill to use Lambda.

AWS Lambda Setup

Go to the AWS Console and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda.
Click on the Create a Lambda Function or Get Started Now button.
Skip the blueprint
Name the Lambda Function "IntegratedHome".
Select the runtime as Java 8
Import this project into Eclipse as an existing Maven Project.
Go to the the samples/ directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" in the target directory.
Select Code entry type as "Upload a .ZIP file" and then upload the "IntegratedHomeProject-1.0-jar-with-dependencies.jar" file from the build directory to Lambda
Set the Handler as integratedHomeProject.IntegratedHomeSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip).
Create a basic execution role and click create.
Leave the Advanced settings as the defaults.
Click "Next" and review the settings then click "Create Function"
Click the "Event Sources" tab and select "Add event source"
Set the Event Source type as Alexa Skills kit and Enable it now. Click Submit.
Copy the ARN from the top right to be used later in the Alexa Skill Setup.
Alexa Skill Setup

Go to the Alexa Console and click Add a New Skill.
Set "Integrated Home" as the skill name and "integrated home" as the invocation name, this is what is used to activate your skill. For example you would say: "Alexa, Ask integrated home to tuen on white light."
Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
Copy the Intent Schema from the included IntentSchema.json.
Copy the Sample Utterances from the included SampleUtterances.txt. Click Next.
Go back to the skill Information tab and copy the appId. Paste the appId into the IntegratedHomeSpeechletRequestStreamHandler.java file for the variable supportedApplicationIds, then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
You are now able to start testing your sample skill! You should be able to go to the Echo webpage and see your skill enabled.
In order to test it, try to say some of the Sample Utterances from the Examples section below.
Your skill is now saved and once you are finished testing you can continue to publish your skill.

Example:

User: "Alexa, ask Integrated Home." 
Alexa: "Welcome to Integrated Home,These are the things Integrated Home can do. Check what devices are turned on. Turn on a device, turn off a device. To check what devices are on say, retrieve. To turn off or turn on a device say, turn off or turn on followed by the device name. For example to turn off light 1 say turn off light 1."
User: "turn on white light."
Alexa: "I will change the state for white light. To confirm change say YES."
User: "yes"
Alexa: white light is now OFF. Goodbye.

