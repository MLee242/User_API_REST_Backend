package com.appdeveloperblog.app.ws.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.appdeveloperblog.app.ws.model.response.UserRest;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appdeveloperblog.app.ws.shared.dto.UserDto;



class UserControllerTest {

	@InjectMocks
	UserController userController;
	
	@Mock
	UserService userService;
	
	UserDto userDto;
	
	final String USER_ID ="12J3K1BHQWLQghas";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto = new UserDto();
		userDto.setFirstName("Moon");
		userDto.setLastName("Lee");
		userDto.setEmail("buddybuddymoon@gmail.com");
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setEmailVerificationToken(null);
		userDto.setUserId(USER_ID);
		userDto.setAddresses(getAddressesDto());
		userDto.setEncryptedPassword("xfkaslkdal");
		
	}
	private List<AddressDto> getAddressesDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		addressDto.setCity("Dearbon");
		addressDto.setCountry("USA");
		addressDto.setStreetName("123 Street Name");
		addressDto.setPostalCode("12345");

		AddressDto billingaddressDto = new AddressDto();
		addressDto.setType("billing");
		addressDto.setCity("Dearbon");
		addressDto.setCountry("USA");
		addressDto.setStreetName("123 Street Name");
		addressDto.setPostalCode("12345");

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingaddressDto);
		return addresses;

	}
	@Test
	void testGetUser() {

		when(userService.getUserById(anyString())).thenReturn(userDto);
		
		
		UserRest result = userController.getUser(USER_ID);
		assertNotNull(result);
		assertEquals(USER_ID, result.getUserId());
		assertEquals(userDto.getFirstName(), result.getFirstName());
		assertTrue(userDto.getAddresses().size() == result.getAddresses().size());
		
		
	}

}
