package com.appdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.appdeveloperblog.app.ws.entity.AddressEntity;
import com.appdeveloperblog.app.ws.entity.UserEntity;
import com.appdeveloperblog.app.ws.repository.AddressRepository;
import com.appdeveloperblog.app.ws.repository.UserRepository;
import com.appdeveloperblog.app.ws.service.AddressService;
import com.appdeveloperblog.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) return returnValue;
		
		
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto returnValue = new AddressDto();
		ModelMapper modelMapper = new ModelMapper();
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if(addressEntity == null) {
			throw new UsernameNotFoundException("Address with ID " +addressId + " NOT FOUND");
		}
		returnValue = modelMapper.map(addressEntity, AddressDto.class);
		return returnValue;
		
		
	}

}
