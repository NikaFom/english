package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.exceptions.CustomException;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.db.repository.StudentRepository;
import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.StudentToUserRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import com.nikafom.englishAssistant.model.enums.StudentStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private UserService userService;

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createStudent() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentInfoResponse result = studentService.createStudent(request);

        assertEquals(student.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void createStudent_badPhoneNumber() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("89056784536");

        studentService.createStudent(request);
    }

    @Test(expected = CustomException.class)
    public void createStudent_badEmail() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test.gmail.com");

        studentService.createStudent(request);
    }

    @Test(expected = CustomException.class)
    public void createStudent_phoneNumberExists() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(student));

        studentService.createStudent(request);
    }

    @Test(expected = CustomException.class)
    public void createStudent_emailExists() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test@gmail.com");

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(student));

        studentService.createStudent(request);
    }

    @Test
    public void getStudent() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        StudentInfoResponse result = studentService.getStudent(student.getId());

        assertEquals(student.getId(), result.getId());
    }

    @Test
    public void updateStudent() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79058364528");
        request.setEmail("test@gmail.com");

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.updateStudent(student.getId(), request);

        verify(studentRepository, times(1)).save(any(Student.class));
        assertEquals(StudentStatus.UPDATED, student.getStatus());
    }

    @Test(expected = CustomException.class)
    public void updateStudent_badPhoneNumber() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("89056784536");

        Student student = new Student();
        student.setId(1L);

        studentService.updateStudent(student.getId(), request);
    }

    @Test(expected = CustomException.class)
    public void updateStudent_badEmail() {
        StudentInfoRequest request = new StudentInfoRequest();
        request.setPhoneNumber("+79056784536");
        request.setEmail("test.gmail.com");

        Student student = new Student();
        student.setId(1L);

        studentService.updateStudent(student.getId(), request);
    }

    @Test
    public void deleteStudent() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.deleteStudent(student.getId());

        verify(studentRepository, times(1)).save(any(Student.class));
        assertEquals(StudentStatus.DELETED, student.getStatus());
    }

    @Test
    public void getAllStudents() {
        Student student1 = new Student();
        student1.setName("Tom");
        student1.setId(1L);

        Student student2 = new Student();
        student2.setId(2L);

        List<Student> students = List.of(student1, student2);
        Page<Student> pagedStudents = new PageImpl<>(students);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, student1.getName());

        when(studentRepository.findAllByStatusNot(pageRequest, StudentStatus.DELETED))
                .thenReturn(pagedStudents);

        Page<StudentInfoResponse> result = studentService
                .getAllStudents(0, 10, student1.getName(), Sort.Direction.ASC, null);

        assertEquals(pagedStudents.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllStudents_Filtered() {
        Student student1 = new Student();
        student1.setName("Tom");
        student1.setId(1L);

        Student student2 = new Student();
        student2.setId(2L);

        List<Student> students = List.of(student1, student2);
        Page<Student> pagedStudents = new PageImpl<>(students);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, student1.getName());

        when(studentRepository.findAllByStatusNotFiltered(pageRequest, StudentStatus.DELETED, "A"))
                .thenReturn(pagedStudents);

        Page<StudentInfoResponse> result = studentService
                .getAllStudents(0, 10, student1.getName(), Sort.Direction.ASC, "A");

        assertEquals(pagedStudents.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addStudentToUser() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        User user = new User();
        user.setId(1L);

        when(userService.getUserFromDB(user.getId())).thenReturn(user);

        StudentToUserRequest request = new StudentToUserRequest();
        request.setUserId(user.getId());
        request.setStudentId(student.getId());

        studentService.addStudentToUser(request);

        verify(studentRepository, times(1)).save(any(Student.class));
        assertEquals(user.getId(), student.getUser().getId());
    }

    @Test
    public void getUserStudents() {
        User user = new User();
        user.setId(1L);

        when(userService.getUserFromDB(user.getId())).thenReturn(user);

        List<Student> students = new ArrayList<>();

        when(studentRepository.findAllByUserId(user.getId())).thenReturn(students);

        List<StudentInfoResponse> result = studentService.getUserStudents(user.getId());

        assertEquals(students.size(), result.size());
    }
}