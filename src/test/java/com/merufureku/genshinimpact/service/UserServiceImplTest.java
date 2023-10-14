package com.merufureku.genshinimpact.service;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.mapper.NewUserMapper;
import com.merufureku.genshinimpact.repository.UsersRepository;
import com.merufureku.genshinimpact.service.exception.ResourceNotFoundException;
import com.merufureku.genshinimpact.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    UsersRepository usersRepository;

    @Mock
    NewUserMapper userMapper;

    @InjectMocks
    UserServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers(){
        List<UsersDTO> usersDTOList = Arrays.asList(new UsersDTO(), new UsersDTO());

        when(usersRepository.findAll()).thenReturn(usersDTOList);

        List<UsersDTO> actual = service.getAllUsers();

        assertNotNull(actual);
        assertEquals(usersDTOList.size(), actual.size());

        verify(usersRepository, atMostOnce()).findAll();
    }

    @Test
    public void testGetUser(){

        UsersDTO expectedUserDTO = new UsersDTO();
        expectedUserDTO.setUsername("Name");
        expectedUserDTO.setPassword("Password");
        expectedUserDTO.setUid(123);

        when(usersRepository.findByUid(123)).thenReturn(expectedUserDTO);

        UsersDTO actual = service.getUser(123);

        assertEquals(123, actual.getUid());
        assertEquals("Name", actual.getUsername());

        verify(usersRepository, atMostOnce()).findByUid(anyInt());
    }

    @Test
    public void testGetUser_whenNull_thenThrowsError() {
        // Stub the user repository to throw an exception when the findByUid() method is called
        when(usersRepository.findByUid(anyInt())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, anyString()));

        // Assert that a RuntimeException is thrown when the getUser() method is called
        assertThrows(ResponseStatusException.class, () -> service.getUser(1));

        verify(usersRepository, atMostOnce()).findByUid(anyInt());
    }

}
