package cms.project.service.impl;

import cms.project.entity.User;
import cms.project.enums.Status;
import cms.project.exceptions.UserNotFoundException;
import cms.project.model.dto.course.response.MailResponse;
import cms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public void approveTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));
        String email = teacher.getEmail();
        teacher.setStatus(Status.APPROVED);
        userRepository.save(teacher);
        MailResponse mailBody = new MailResponse(email, "Account update", "Your account is activated and ready for use");
        emailService.sendMail(mailBody);

    }

    public void rejectTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));
        teacher.setStatus(Status.REJECTED);
        userRepository.save(teacher);
    }
}
