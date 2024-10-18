package com.finalProject.Back.service;

import com.finalProject.Back.dto.request.User.ReqOAuth2MergeDto;
import com.finalProject.Back.dto.request.User.ReqSigninDto;
import com.finalProject.Back.dto.request.User.ReqSignupDto;
import com.finalProject.Back.dto.response.User.RespSigninDto;
import com.finalProject.Back.dto.response.User.RespSignupDto;
import com.finalProject.Back.dto.response.User.RespUserInfoDto;
import com.finalProject.Back.entity.OAuth2User;
import com.finalProject.Back.entity.User;
import com.finalProject.Back.repository.UserMapper;
import com.finalProject.Back.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    public RespSignupDto userSignup(ReqSignupDto dto) {
        User user = null;

        user = dto.toEntity(passwordEncoder);
        userMapper.save(user);

        return RespSignupDto.builder()
                .message("가입하신 이메일 주소를 통해 인증 후 사용할 수 있습니다.")
                .user(user)
                .build();
    }

    public RespSigninDto generaterAccessToken (ReqSigninDto dto){
        User user = checkUsernameAndPassword(dto.getUsername(), dto.getPassword());

        return RespSigninDto.builder()
                .expireDate(jwtProvider.getExpireDate().toString())
                .accessToken(jwtProvider.generateAccessToken(user))
                .build();
    }

    private User checkUsernameAndPassword(String username, String password) {
        User user = userMapper.findByUsername(username);
        System.out.println(user);

        if(user == null) {
            throw new UsernameNotFoundException("사용자 정보를 다시 확인하세요.");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 확인하세요.");
        }

        return user;
    }

    public Boolean isDuplicateUsername(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username)).isPresent();
    }

    public OAuth2User mergeSignin(ReqOAuth2MergeDto dto) {
    User user = checkUsernameAndPassword(dto.getUsername(), dto.getPassword());
        System.out.println(user);
    return OAuth2User.builder()
            .userId(user.getId())
            .oAuth2Name(dto.getOauth2Name())
            .provider(dto.getProvider())
            .build();
    }

    public RespUserInfoDto getUserInfo(Long id) {
        log.info("{}" , id);
        User user = userMapper.findById(id);
        return RespUserInfoDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .img(user.getImg())
                .role(user.getRole())
                .build();
    }

}
