#Setup
To run this skill you need to do two things. The first is to deploy the code in lambda, and the second is to configure the Alexa skill to use Lambda.

#AWS Lambda Setup
1.Go to the AWS Console and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda. 

2.Click on the Create a Lambda Function or Get Started Now button. Skip the blueprint Name the Lambda Function "IntegratedHome".

3.Select the runtime as Java 8.

4.Import this project into Eclipse as an existing Maven Project.

5.Go to the the samples/ directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'.

6.This will generate a zip file named "IntegratedHomeProject-1.0-jar-with-dependencies.jar" in the target directory.

7.Select Code entry type as "Upload a .ZIP file" and then upload the "IntegratedHomeProject-1.0-jar-with-dependencies.jar" file from the build directory to Lambda Set the Handler as integratedHomeProject.IntegratedHomeSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip). 

8.Create a basic execution role and click create.

9.Leave the Advanced settings as the defaults. 

10.Click "Next" and review the settings then click "Create Function" Click the "Event Sources" tab and select "Add event source" Set the Event Source type as Alexa Skills kit and Enable it now. 

11.Click Submit. Copy the ARN from the top right to be used later in the Alexa Skill Setup. 

#Alexa Skill Setup
1.Go to the Alexa Console and click Add a New Skill. 

2.Set "Integrated Home" as the skill name and "integrated home" as the invocation name, this is what is used to activate your skill. 

3.For example, you would say: "Alexa, Ask integrated home to turn on white light." 

4.Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next. 

5.Copy the custom slot types from the customSlotTypes Folder. Each file in the folder represents a new custom slot type. The name of the file is the name of the custom slot type, and the values in the file are the values for the custom slot.

6.Copy the Intent Schema from the included IntentSchema.json. 

7.Copy the Sample Utterances from the included SampleUtterances.txt. Click Next. 

8.Go back to the skill Information tab and copy the appId. Paste the appId into the IntegratedHomeSpeechletRequestStreamHandler.java file for the variable supportedApplicationIds, then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source. 

9.You are now able to start testing your sample skill! 

10.You should be able to go to the Echo webpage and see your skill enabled. 

11.In order to test it, try to say some of the Sample Utterances from the Examples section below.

12.Your skill is now saved and once you are finished testing you can continue to publish your skill.

#Example:
User: "Alexa, ask Integrated Home."

Alexa: "Welcome to Integrated Home, These are the things Integrated Home can do. Check what devices are turned on. Turn on a device, turn off a device. To check what devices are on say, retrieve. To turn off or turn on a device say, turn off or turn on followed by the device name. For example, to turn off light 1 say turn off light 1."

User: "turn on white light."

Alexa: "I will change the state for white light. To confirm change, say YES."

User: "yes"

Alexa: white light is now OFF. Goodbye.
