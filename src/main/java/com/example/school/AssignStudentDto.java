package com.example.school;

import javax.persistence.Id;
import java.util.UUID;

public class AssignStudentDto {

    @Id
    private UUID studentID;

    @Id
    private UUID teacherID;
}
