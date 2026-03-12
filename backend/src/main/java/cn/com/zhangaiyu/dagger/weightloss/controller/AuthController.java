package cn.com.zhangaiyu.dagger.weightloss.controller;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.LoginRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.request.UserRegistrationRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ApiResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.JwtResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.UserResponse;
import cn.com.zhangaiyu.dagger.weightloss.security.JwtTokenProvider;
import cn.com.zhangaiyu.dagger.weightloss.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse user = userService.registerUser(request);
        return ApiResponse.success("User registered successfully", user);
    }

    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        UserResponse user = userService.getUserByUsername(userDetails.getUsername());
        JwtResponse jwtResponse = new JwtResponse(token, user);

        return ApiResponse.success("Login successful", jwtResponse);
    }
}
