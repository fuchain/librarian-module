package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.entities.Role;
import com.fpt.edu.exceptions.EntityIdMismatchException;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController {

	@Autowired
	private RoleServices roleServices;

	public RoleController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, publishSubscribe, requestQueueManager, notificationService);
	}

	@ApiOperation(value = "Get role by id", response = JSONObject.class)
	@GetMapping("{id}")
	public ResponseEntity<JSONObject> getById(@PathVariable Long id) {
		Role role = roleServices.getById(id);

		JSONObject jsonResult = new JSONObject(role);

		return new ResponseEntity<>(jsonResult, HttpStatus.OK);
	}

	@ApiOperation(value = "Create a role ", response = JSONObject.class)
	@PostMapping("")
	public ResponseEntity<JSONObject> createRole(@RequestBody Role role) {
		Role addedRole = roleServices.addRole(role);
		JSONObject jsonResult = new JSONObject(addedRole);

		return new ResponseEntity<>(jsonResult, HttpStatus.OK);
	}

	@ApiOperation(value = "Update a role ", response = JSONObject.class)
	@PutMapping("/{id}")
	public ResponseEntity<JSONObject> updateRole(@RequestBody Role role, @PathVariable Long id) throws EntityIdMismatchException {
		if (!role.getId().equals(id)) {
			throw new EntityIdMismatchException("Book ID: " + id + " and " + role.getId() + " does not match");
		}

		Role existedRole = roleServices.getById(role.getId());
		Role updatedRole = roleServices.updateRole(existedRole);

		JSONObject jsonResult = new JSONObject(updatedRole);
		return new ResponseEntity<>(jsonResult, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a role ", response = JSONObject.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> deleteRole(@PathVariable Long id) {

		Role existedRole = roleServices.getById(id);
		roleServices.deleteRole(existedRole.getId());

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("message", "success");
		return new ResponseEntity<>(jsonResult, HttpStatus.OK);
	}

}
