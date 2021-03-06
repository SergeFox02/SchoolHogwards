package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private List<Student> students;

    @Autowired
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for creat student");
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by id = {}", id);
        return studentRepository.findById(id).get();
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by id = {}", id);
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    @Override
    public Collection<Student> filterAgeStudents(int age) {
        logger.info("Was invoked method for filter Students by age = {}", age);
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Student> filterAgeStudents(int min, int max) {
        logger.info("Was invoked method for filter Students by age between {} and {} age", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty findFacultyOfStudent(long id) {
        logger.info("Was invoked method for find faculty by id = {} of Student", id);
        return studentRepository.getById(id).getFaculty();
    }

    @Override
    public long getAmountOfStudents() {
        logger.info("Was invoked method for get amount of students");
        return studentRepository.getAmountOfStudents();
    }

    @Override
    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students ");
        return studentRepository.getAverageAge();
    }

    @Override
    public Collection<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students by id");
        return studentRepository.getFiveLastStudents();
    }

    @Override
    public Collection<String> filterStudentsBuOrderStartNameA() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().startsWith("A"))
                .map(s -> s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public OptionalDouble averageAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(s -> (double) s.getAge())
                .average();
    }

    @Override
    public int findValue() {
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b );
        return sum;
    }

    @Override
    public void printAllStudentsToConsole() {
        System.out.println("Print students to console by Threads:");
        students = new ArrayList<>(studentRepository.findAll());

        printNameOfStudent(0);
        printNameOfStudent(1);

        new Thread(() -> {
            printNameOfStudent(2);
            printNameOfStudent(3);
        }).start();

        new Thread(() -> {
            printNameOfStudent(4);
            printNameOfStudent(5);
        }).start();
    }

    @Override
    public void printAllStudentsToConsoleSynchronized() {
        System.out.println("Print students to console by Synchronized Threads:");
        students = new ArrayList<>(studentRepository.findAll());

        printNameOfStudentSynchronized(0);
        printNameOfStudentSynchronized(1);

        new Thread(() -> {
            printNameOfStudentSynchronized(2);
            printNameOfStudentSynchronized(3);
        }).start();

        new Thread(() -> {
            printNameOfStudentSynchronized(4);
            printNameOfStudentSynchronized(5);
        }).start();
    }

    private void printNameOfStudent(int numberOfStudent) {
        System.out.println(numberOfStudent + " " + students.get(numberOfStudent).getName());
//        String s = "";
//        for (int i = 0; i < 100_000; i++) {
//            s += i;
//        }
    }

    private synchronized void printNameOfStudentSynchronized(int numberOfStudent) {
        System.out.println(numberOfStudent + " " + students.get(numberOfStudent).getName());
//        String s = "";
//        for (int i = 0; i < 100_000; i++) {
//            s += i;
//        }
    }
}
