package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.exceptions.CustomException;
import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.db.repository.UserRepository;
import com.nikafom.englishAssistant.model.dto.request.UserInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.UserInfoResponse;
import com.nikafom.englishAssistant.model.enums.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserInfoResponse result = userService.createUser(request);

        assertEquals(user.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void createUser_badPhoneNumber() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("89056784536");

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test.gmail.com");

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_phoneNumberExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_emailExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserInfoResponse result = userService.getUser(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void updateUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79058364528");
        request.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.updateUser(user.getId(), request);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.UPDATED, user.getStatus());
    }

    @Test(expected = CustomException.class)
    public void updateUser_badPhoneNumber() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("89056784536");

        User user = new User();
        user.setId(1L);

        userService.updateUser(user.getId(), request);
    }

    @Test(expected = CustomException.class)
    public void updateUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test.gmail.com");

        User user = new User();
        user.setId(1L);

        userService.updateUser(user.getId(), request);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void getAllUsers() {
        User user1 = new User();
        user1.setName("Tom");
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        List<User> users = List.of(user1, user2);
        Page<User> pagedUsers = new PageImpl<>(users);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, user1.getName());

        when(userRepository.findAllByStatusNot(pageRequest, UserStatus.DELETED)).thenReturn(pagedUsers);

        Page<UserInfoResponse> result = userService
                .getAllUsers(0, 10, user1.getName(), Sort.Direction.ASC, null);

        assertEquals(pagedUsers.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllUsers_Filtered() {
        User user1 = new User();
        user1.setName("Tom");
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        List<User> users = List.of(user1, user2);
        Page<User> pagedUsers = new PageImpl<>(users);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, user1.getName());

        when(userRepository.findAllByStatusNotFiltered(pageRequest, UserStatus.DELETED, "A"))
                .thenReturn(pagedUsers);

        Page<UserInfoResponse> result = userService
                .getAllUsers(0, 10, user1.getName(), Sort.Direction.ASC, "A");

        assertEquals(pagedUsers.getTotalElements(), result.getTotalElements());
    }
}