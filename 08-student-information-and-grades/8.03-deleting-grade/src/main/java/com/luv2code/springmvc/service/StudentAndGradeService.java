package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String emailAddress){

        CollegeStudent student = new CollegeStudent(firstName, lastName, emailAddress);
        student.setId(0);
        studentDao.save(student);

    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if(student.isPresent())
            return true;
        return false;
    }

    public void deleteStudentById(int id) {
        if(this.checkIfStudentIsNull(id)){
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradeDao.deleteByStudentId(id);
        }
    }

    public boolean createGrade(double grade, int studentId, String gradeType){
        if(!checkIfStudentIsNull(studentId)){
            return false;
        }
        if(grade >= 0 && grade <= 100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setStudentId(studentId);
                mathGrade.setGrade(grade);
                mathGradeDao.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setStudentId(studentId);
                scienceGrade.setGrade(grade);
                scienceGradeDao.save(scienceGrade);
                return true;
            }
            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setStudentId(studentId);
                historyGrade.setGrade(grade);
                historyGradeDao.save(historyGrade);
                return true;
            }

        }
        return false;
    }


    public Iterable<CollegeStudent> getGradebook() {
        return studentDao.findAll();
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;
        if(gradeType.equals("math")){
            Optional<MathGrade> grade = mathGradeDao.findById(id);
            if(!grade.isPresent())
                return studentId;
            studentId = grade.get().getStudentId();
            mathGradeDao.deleteById(studentId);
        }
        if(gradeType.equals("science")){
            Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
            if(!grade.isPresent())
                return studentId;
            studentId = grade.get().getStudentId();
            scienceGradeDao.deleteById(studentId);
        }
        if(gradeType.equals("history")){
            Optional<HistoryGrade> grade = historyGradeDao.findById(id);
            if(!grade.isPresent())
                return studentId;
            studentId = grade.get().getStudentId();
            historyGradeDao.deleteById(studentId);
        }
        return studentId;
    }

    public GradebookCollegeStudent studentInformatiom(int id) {

        if(!checkIfStudentIsNull(id)){
            return null;
        }

        Optional<CollegeStudent> student = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(),
                student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(), studentGrades);

        return gradebookCollegeStudent;
    }

    public void configureStudentInformationMode(int id, Model m){
        GradebookCollegeStudent studentEntity = studentInformatiom(id);
        m.addAttribute("student", studentEntity);

        if(studentEntity.getStudentGrades().getMathGradeResults().size() > 0){
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getMathGradeResults()
            ));
        }
        else{
            m.addAttribute("mathAverage", "N/A");
        }

        if(studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0){
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getHistoryGradeResults()
            ));
        }
        else{
            m.addAttribute("historyAverage", "N/A");
        }

        if(studentEntity.getStudentGrades().getScienceGradeResults().size() > 0){
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getScienceGradeResults()
            ));
        }
        else{
            m.addAttribute("scienceAverage", "N/A");
        }


    }

}

















