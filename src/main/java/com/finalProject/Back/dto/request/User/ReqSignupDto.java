package com.finalProject.Back.dto.request.User;

import com.finalProject.Back.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ReqSignupDto {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{8,}$", message = "사용자이름은 8자이상의 영소문자 , 숫자 조합이여야합니다.")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8자이상 16자 이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함해야 합니다.")
    private String password;
    @NotBlank(message = "비밀번호 공백일 수 없습니다")
    private String checkPassword;
    @NotBlank
    @Pattern(regexp = "^[가-힣]+$", message = "한글로 된 이름을 기입해주세요.")
    private String name;
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "이메일 형식이어야 합니다")
    private String email;
    @NotBlank
    @Pattern(regexp = "^.{1,10}$", message = "닉네임은 10글자 이내여야 하고 공백일 수 없습니다.")
    private String nickname;
    @NotBlank(message = "전화번호 인증을 받아야 합니다.")
    private String phoneNumber;
    private String role;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }

    @AssertTrue(message="비밀번호를 일치해주세요")
    private boolean isPasswordMatching() {
        return password != null&& password.equals(checkPassword);
    }
}
