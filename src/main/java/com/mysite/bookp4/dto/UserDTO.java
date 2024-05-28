package com.mysite.bookp4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long user_id;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    @NotBlank(message = "비밀번호확인을 입력해주세요")
    private String password2;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
}
