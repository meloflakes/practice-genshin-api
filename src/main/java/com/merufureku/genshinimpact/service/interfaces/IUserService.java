package com.merufureku.genshinimpact.service.interfaces;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;

import java.util.List;

public interface IUserService {

    List<UsersDTO> getAllUsers();

    UsersDTO getUser(Integer uid);

    UsersDTO createUser(NewUserParam newUserParam);

    void deleteUser(Integer uid);

    UsersDTO updateUserLoginDate(Integer uid);
}
