package com.jtech.apps.hcm.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.jtech.apps.hcm.model.UserProfile;
import com.jtech.apps.hcm.service.UserProfileService;

@RestController
public class UserProfileController {
	
	@Autowired
	UserProfileService userProfileService;
	
	private static final Logger logger = Logger.getLogger(UserProfileController.class);

	@RequestMapping(value = "/userprofile/test/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProfile> getTestData() {

		UserProfile userProfile = userProfileService.getTestData();

		if (userProfile == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);
	}

	@RequestMapping(value = "/userprofile/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserProfile>> getUserProfiles() {

		logger.info("Userprofile get");

		List<UserProfile> userProfiles = userProfileService.getUserProfiles();

		if (userProfiles == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<List<UserProfile>>(userProfiles, HttpStatus.OK);

	}

	@RequestMapping(value = "/userprofile/get/id/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProfile> getUserProfileById(@PathVariable("userid") Integer userId) {

		logger.info("Userprofile get by id");

		UserProfile userProfile = userProfileService.getUserProfileByUserId(userId);

		if (userProfile == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);

	}

	@RequestMapping(value = "/userprofile/get/name/{username:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProfile> getUserProfileByName(@PathVariable("username") String userName) {

		logger.info("Userprofile get by name");

		UserProfile userProfile = userProfileService.getUserProfileByUserName(userName);

		if (userProfile == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);

	}

	@RequestMapping(value = "/userprofile/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> updateUserProfile(@RequestBody UserProfile userProfile) {

		logger.info("Userprofile update");

		int err = userProfileService.updateUserProfile(userProfile);
		return new ResponseEntity<Integer>(err, HttpStatus.OK);
	}

	@RequestMapping(value = "/userprofile/add", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> addUserProfile(@RequestBody UserProfile userProfile) {

		logger.info("Userprofile add");

		int err = userProfileService.addUserProfile(userProfile);
		return new ResponseEntity<Integer>(err, HttpStatus.OK);
	}
}
