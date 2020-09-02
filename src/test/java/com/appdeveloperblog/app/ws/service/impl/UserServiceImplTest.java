package com.appdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appdeveloperblog.app.ws.entity.AddressEntity;
import com.appdeveloperblog.app.ws.entity.UserEntity;
import com.appdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appdeveloperblog.app.ws.repository.UserRepository;
import com.appdeveloperblog.app.ws.shared.Utils;
import com.appdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	String userId = "hty57efhs";
	String encryptedPassword = "183718hasdajehqfjf";

	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Moon");
		userEntity.setLastName("Lee");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");
		assertNotNull(userDto);
		assertEquals("Moon", userDto.getFirstName());

	}

	@Test
	final void testGetUser_UsernameNotFoundException() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class,

				() -> {
					userService.getUser("test@test.com");
				}

		);

	}
	
	@Test
	final void testGetUser_UserServiceException() {

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setEmail("test@test.com");
		userDto.setFirstName("Moon");
		userDto.setLastName("Lee");
		userDto.setPassword("12345678");
		
		
		
		assertThrows(UserServiceException.class,

				() -> {
					userService.createUser(userDto);
				}

		);

	}
	

	@Test
	final void testCreateUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);

		when(utils.generateAddressId(anyInt())).thenReturn("hgfh123nuwe848");

		when(utils.generateUserId(anyInt())).thenReturn(userId);

		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);

		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setEmail("test@test.com");
		userDto.setFirstName("Moon");
		userDto.setLastName("Lee");
		userDto.setPassword("12345678");

		UserDto storedUserDetails = userService.createUser(userDto);

		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());

		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
	
		verify(utils,times(2)).generateAddressId(30);
		verify(bCryptPasswordEncoder,times(1)).encode("12345678");
		verify(userRepository,times(1)).save(any(UserEntity.class));
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
	
	private List<AddressEntity> getAddressesEntity(){
		List<AddressDto> addresses = getAddressesDto();
		//convert List<AddressDto> to List<AddressEntity>
		java.lang.reflect.Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		List<AddressEntity> resultList = new ModelMapper().map(addresses, listType);
		return resultList;
		
	}

}
