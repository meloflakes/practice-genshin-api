package com.merufureku.genshinimpact.service.impl;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;
import com.merufureku.genshinimpact.mapper.NewUserMapper;
import com.merufureku.genshinimpact.repository.UsersRepository;
import com.merufureku.genshinimpact.service.exception.ResourceNotFoundException;
import com.merufureku.genshinimpact.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements IUserService {

    private final UsersRepository usersRepository;
    private final NewUserMapper userMapper;
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, NewUserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UsersDTO getUser(Integer uid) {

        logger.info("Get account of UID: " + uid);

        throw404IfNull(uid.longValue());
        UsersDTO usersDTO = usersRepository.findByUid(uid);

        logger.info("UID " + uid + " account is: " + usersDTO);
        return usersDTO;
    }

    @Override
    public List<UsersDTO> getAllUsers() {

        List<UsersDTO> usersDTOList = usersRepository.findAll();

        return usersDTOList;
    }

    @Override
    public UsersDTO createUser(NewUserParam userParam){
        if (usersRepository.findByEmailAndUsername(userParam.getEmail(),
                userParam.getUsername()) != null){

            logger.info("User already exist!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email or Username already saved!");
        }

        logger.info("Saving the following details: " + userParam);
        UsersDTO newUser = userMapper.mapUser(userParam);

        usersRepository.save(newUser);

        return getUser(newUser.getUid());
    }

    @Override
    public UsersDTO updateUserLoginDate(Integer uid){

        UsersDTO usersDTO = getUser(uid);
        usersDTO.setLast_login_date(LocalDate.now());

        try{
            logger.info("Updating account uid: " + uid);
            usersRepository.save(usersDTO);
        }
        catch (RuntimeException e){
            throw new RuntimeException("Unable to save data");
        }

        return usersDTO;
    }

    @Override
    public void deleteUser(Integer uid){
        throw404IfNull(uid.longValue());
        usersRepository.deleteById(uid.longValue());
    }

    private void throw404IfNull(Long uid){
        if (!usersRepository.existsById(uid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
