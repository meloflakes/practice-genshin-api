package com.merufureku.genshinimpact.dto.params;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewUserParam {

    @NotEmpty
    @NotNull(message = "Username is required")
    private String username;

    @NotEmpty
    @NotNull(message = "Email is required")
    private String email;

    @NotEmpty
    @NotNull(message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "NewUserParam{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
