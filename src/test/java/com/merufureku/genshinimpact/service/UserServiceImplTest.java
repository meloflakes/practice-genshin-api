package com.merufureku.genshinimpact.service;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;
import com.merufureku.genshinimpact.mapper.NewUserMapper;
import com.merufureku.genshinimpact.repository.UsersRepository;
import com.merufureku.genshinimpact.service.exception.ResourceNotFoundException;
import com.merufureku.genshinimpact.service.impl.UserServiceImpl;
import org.apache.catalina.User;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final static Integer uid = 6969696;
    private final static String username = "merufureku";
    private final static String email = "merufureku@email.com";
    private final static String password = "P@s$vv0Rd";
    private final static LocalDate dateToday = LocalDate.now();

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UsersRepository usersRepository;

    @Mock
    NewUserMapper userMapper;

    UsersDTO expectedUserDTO = new UsersDTO();

    NewUserParam newUserParam = new NewUserParam();

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        expectedUserDTO.setUsername(username);
        expectedUserDTO.setEmail(email);
        expectedUserDTO.setUid(uid);
        expectedUserDTO.setCreated_date(dateToday);

        newUserParam.setEmail(email);
        newUserParam.setUsername(username);
        newUserParam.setPassword(password);
    }

    @Test
    public void testGetAllUsers(){
        List<UsersDTO> usersDTOList = Arrays.asList(new UsersDTO(), new UsersDTO(), new UsersDTO());

        when(usersRepository.findAll()).thenReturn(usersDTOList);

        List<UsersDTO> actual = service.getAllUsers();

        assertNotNull(actual);
        assertEquals(usersDTOList.size(), actual.size());

        verify(usersRepository, atMostOnce()).findAll();
    }

    @Test
    public void testGetAllUsers_whenNull_thenReturnNone(){
        when(usersRepository.findAll()).thenReturn(null);

        List<UsersDTO> actual = service.getAllUsers();

        assertNull(actual);

        verify(usersRepository, atMostOnce()).findAll();
    }

    @Test
    public void testGetUser(){

        when(usersRepository.findByUid(uid)).thenReturn(expectedUserDTO);
        when(usersRepository.existsById(uid.longValue())).thenReturn(true);

        UsersDTO actual = service.getUser(uid);

        assertEquals(uid, actual.getUid());
        assertEquals(username, actual.getUsername());
        assertEquals(email, actual.getEmail());

        verify(usersRepository, atMostOnce()).findByUid(anyInt());
        verify(usersRepository, atMostOnce()).existsById(anyLong());
    }

    @Test
    public void testGetUser_whenNull_thenThrowsError() {
        when(usersRepository.findByUid(anyInt())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, anyString()));

        assertThrows(ResponseStatusException.class, () -> service.getUser(1));

        verify(usersRepository, atMostOnce()).findByUid(anyInt());
    }

    @Test
    public void testCreateNewUser(){

        when(usersRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(null);
        when(userMapper.mapUser(any(NewUserParam.class))).thenReturn(expectedUserDTO);
        when(usersRepository.save(new UsersDTO())).thenReturn(expectedUserDTO);
        when(usersRepository.existsById(anyLong())).thenReturn(true);
        when(service.getUser(uid)).thenReturn(expectedUserDTO);

        UsersDTO actual = service.createUser(newUserParam);

        assertNotNull(actual);
        assertEquals(uid, actual.getUid());
        assertEquals(email, actual.getEmail());
        assertEquals(username, actual.getUsername());
        assertEquals(dateToday, actual.getCreated_date());

        verify(userMapper, atMostOnce()).mapUser(any(NewUserParam.class));
        verify(usersRepository, atMostOnce()).findByEmailAndUsername(anyString(), anyString());
        verify(usersRepository, atMost(2)).existsById(anyLong());
    }

    @Test
    public void testCreateNewUser_whenExisting_thenThrowException(){
        when(usersRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(expectedUserDTO);

        assertThrows(ResponseStatusException.class, () -> service.createUser(newUserParam));

        verify(usersRepository, atMostOnce()).findByEmailAndUsername(anyString(), anyString());
    }

    @Test
    public void testUpdateUserLoginDate(){
        expectedUserDTO.setLast_login_date(dateToday);

        when(usersRepository.existsById(anyLong())).thenReturn(true);
        when(usersRepository.save(any(UsersDTO.class))).thenReturn(expectedUserDTO);
        when(service.getUser(uid)).thenReturn(expectedUserDTO);

        UsersDTO actual = service.updateUserLoginDate(uid);

        assertNotNull(actual);
        assertEquals(dateToday, actual.getLast_login_date());

        verify(usersRepository, atMost(2)).existsById(anyLong());
        verify(usersRepository, atMostOnce()).save(any(UsersDTO.class));
    }

    @Test
    public void testUpdateUserLoginDate_whenUserNotFound_thenThrowException(){
        when(usersRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.deleteUser(anyInt()));

        verify(usersRepository, atMostOnce()).existsById(anyLong());
        verify(usersRepository, atMost(0)).save(any(UsersDTO.class));
    }

    @Test
    public void testDeleteUser(){
        when(usersRepository.existsById(anyLong())).thenReturn(true);

        service.deleteUser(anyInt());

        verify(usersRepository, atMostOnce()).existsById(anyLong());
        verify(usersRepository, atMostOnce()).deleteById(anyLong());
    }

    @Test
    public void testDeleteUser_whenNull_thenThrowException(){
        when(usersRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.deleteUser(anyInt()));

        verify(usersRepository, atMostOnce()).existsById(anyLong());
        verify(usersRepository, atMostOnce()).deleteById(anyLong());
    }
}
