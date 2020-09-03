package com.appdeveloperblog.app.ws.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appdeveloperblog.app.ws.model.request.PasswordRequestModel;
import com.appdeveloperblog.app.ws.model.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ws.model.response.AddressRest;
import com.appdeveloperblog.app.ws.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ws.model.response.RequestOperationName;
import com.appdeveloperblog.app.ws.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ws.model.response.UserRest;
import com.appdeveloperblog.app.ws.service.AddressService;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appdeveloperblog.app.ws.shared.dto.UserDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@ApiOperation(value ="The Get User Details Web Service Endpoint",
			notes ="This Web Service Endpoint returns User Details. User Public user id"
					+ " in URL Path. For example /user/uhf3"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@GetMapping(path="/{userId}", produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public UserRest getUser(@PathVariable("userId") String userId) {
		UserRest userRest = new UserRest();
		
		UserDto userDto = userService.getUserById(userId);
		ModelMapper modelMapper = new ModelMapper();
		userRest = modelMapper.map(userDto, UserRest.class);
		return userRest;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@GetMapping(produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
	})
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue = "0") int page, 
			@RequestParam(value="limit", defaultValue="2") int limit){
		
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		
		//Type listType = new TypeToken<List<UserRest>>() {}.getType();
		//returnValue = new ModelMapper().map(users,listType);
		
		if(users != null && !users.isEmpty()) {
			//Map entire list using generics
			java.lang.reflect.Type listType = new TypeToken<List<UserRest>>() {}.getType();
			returnValue = new ModelMapper().map(users, listType);
			
		}

		/*
		for ( UserDto userDto: users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto,userModel);
			returnValue.add(userModel);
		}
		*/
		
		return returnValue;
		
	}
	
	
	@PostMapping(produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	},consumes = {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
		
		
		//UserDto userDto = new UserDto();
		
		/*if(userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}*/
		
		//BeanUtils.copyProperties(userDetails, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		
		UserDto createdUser = userService.createUser(userDto);
		UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
	
		return returnValue;
		
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@PutMapping(path="/{userId}", produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	},consumes = {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public UserRest updateUser(@PathVariable("userId") String userId,
			@RequestBody UserDetailsRequestModel userDetails) {
		
		UserDto userDto = new UserDto();
		ModelMapper modelMapper= new ModelMapper();
		userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto updatedUser = userService.updateUser(userId, userDto);
		UserRest returnValue = new UserRest();
		
		returnValue = modelMapper.map(updatedUser, UserRest.class);
		return returnValue;
		
		
		
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@DeleteMapping(path="/{userId}", produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public OperationStatusModel deleteUser(@PathVariable("userId") String userId) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		

		userService.deleteUser(userId);
		
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
		
		
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@GetMapping(path="/{userId}/addresses", produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public CollectionModel<AddressRest> getUserAddresses(@PathVariable("userId")String userId){
		
		List<AddressRest> returnValue = new ArrayList<>();
		List<AddressDto> addressesDto = addressService.getAddresses(userId);
		
		if(addressesDto != null && !addressesDto.isEmpty()) {
			//Map entire list using generics
			java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
			
			
			for(AddressRest addressRest: returnValue) {
				Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressRest.getAddressId()))
						//.slash(userId)
						//.slash("addresses")
						//.slash(addressId)
						.withSelfRel();
				
				addressRest.add(selfLink);
				
			}
			
			
		}
		
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.withRel("user");
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
				//.slash(userId)
				//.slash("addresses")
				//.slash(addressId)
				.withSelfRel();
		
		return CollectionModel.of(returnValue, userLink, selfLink);
		
	}
	

	@ApiImplicitParams({
		@ApiImplicitParam(name ="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		
	})
	@GetMapping(path="/{userId}/addresses/{addressId}", produces= {
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE
			
	})
	public EntityModel<AddressRest> getUserAddress(@PathVariable("userId") String userId, @PathVariable("addressId") String addressId){
		
		AddressRest returnValue = new AddressRest();
		AddressDto addressDto = addressService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();
		
		returnValue = modelMapper.map(addressDto, AddressRest.class);
		// http://localhost:8080/mobile-app-ws/users/<userId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.withRel("user");
		Link userAddressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
				//.slash(userId)
				//.slash("addresses")
				.withRel("addresses");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId))
				//.slash(userId)
				//.slash("addresses")
				//.slash(addressId)
				.withSelfRel();
		
		//returnValue.add(userLink);
		//returnValue.add(userAddressLink);
		//returnValue.add(selfLink);
		
		
		
		return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressLink, selfLink));
		
	}
	/*
	@PostMapping(path = "/password-reset-request",
		produces= {
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
				
		},consumes = {
				MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE
				
		}
	)
	public OperationStatusModel requestReset(@RequestBody PasswordRequestModel passwordResetRequestModel) {
			
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
		
		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if(operationResult) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
			
		}
		return returnValue;
	}
	*/
	
	
	
	
}
