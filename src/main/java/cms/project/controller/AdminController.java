package cms.project.controller;

import cms.project.service.impl.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/approve/{teacherId}")
    public ResponseEntity<String> approved(@PathVariable Long teacherId) {
        try {
            adminService.approveTeacher(teacherId);
            return new ResponseEntity<>("Teacher status changed to approved", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reject/{teacherId}")
    public ResponseEntity<String> rejected(@PathVariable Long teacherId) {
        try {
            adminService.rejectTeacher(teacherId);
            return new ResponseEntity<>("Teacher status changed to rejected", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}