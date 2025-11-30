package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.dto.InstructorCoursesReportDTO;

public interface InstructorReportService {
    
    InstructorCoursesReportDTO generateInstructorCoursesReport(Long instructorId);
}