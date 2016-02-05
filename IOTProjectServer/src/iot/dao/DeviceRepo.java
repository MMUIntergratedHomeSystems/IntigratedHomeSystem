package iot.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import iot.mvc.DeviceObject;

public interface DeviceRepo extends MongoRepository<DeviceObject, String>{//, QueryDslPredicateExecutor<DeviceObject>{
	@Query("{'deviceID' : ?0}")
	List<DeviceObject> findDeviceByID(String deviceID);

	List<DeviceObject> getAll();
	
	// Find by room
	// Find by type

}
