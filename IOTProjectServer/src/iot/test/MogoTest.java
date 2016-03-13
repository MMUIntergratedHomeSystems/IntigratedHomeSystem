package iot.test;

//import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;

import iot.dao.SpringMongoConfig;
import iot.models.DeviceModel;
import iot.models.StateModel;

public class MogoTest {

	public static void main(String[] args){

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		StateModel state = new StateModel("Test3", "off", new Date());
		mongoOperation.save(state);
		state = new StateModel("Test3", "on", new Date());
		mongoOperation.save(state);
//		state = new StateObject("Test", "off", new Date());
//		mongoOperation.save(state);

		List<StateModel> listofstates = mongoOperation.findAll(StateModel.class);
		System.out.println(listofstates);

		//		ArrayList<StateObject> test = new ArrayList<StateObject>();
		//		test.add(state);
		//		try {
		//			Thread.sleep(4000);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		state.setState("off");
		//		state.setDateStored(new Date());
		//		test.add(state);

		DeviceModel Device = new DeviceModel("Test2", null, "home/things/light", "side light", null, null, false, null);

		// save
		mongoOperation.save(Device);

		// Print object
		System.out.println("Device: "+Device);

		// search
		Query searchDeviceQuery = new Query(Criteria.where("DeviceID").is("Test"));

		// find the device
		DeviceModel savedDevice = mongoOperation.findOne(searchDeviceQuery, DeviceModel.class);
		System.out.println("Saved Device: "+savedDevice);



		// update state
		//		test.clear();
		//		state.setState("off");
		//		state.setDateStored(new Date());
		//		test.add(state);
		//		
		//		mongoOperation.updateFirst(searchDeviceQuery, Update.update("State", test), DeviceObject.class);
		//		
		//		// find updated
		//		DeviceObject updatedDevice = mongoOperation.findOne(searchDeviceQuery, DeviceObject.class);
		//		System.out.println("Updated: "+updatedDevice);

		//delete
	//	mongoOperation.remove(searchDeviceQuery, DeviceObject.class);

		// show number of entries
		List<DeviceModel> DeviceList = mongoOperation.findAll(DeviceModel.class);
		System.out.println("Number of entries"+ DeviceList.size());

		((ConfigurableApplicationContext)ctx).close();
	}

}
