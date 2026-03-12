package cn.com.zhangaiyu.dagger.weightloss.controller;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.UserUpdateRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ApiResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.UserResponse;
import cn.com.zhangaiyu.dagger.weightloss.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        return ApiResponse.success(user);
    }

    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateUserProfile(
            Authentication authentication,
            @RequestBody UserUpdateRequest request) {
        String username = authentication.getName();
        UserResponse currentUser = userService.getUserByUsername(username);
        UserResponse updatedUser = userService.updateUser(currentUser.getId(), request);
        return ApiResponse.success("Profile updated successfully", updatedUser);
    }
}
