package com.example.demo.services.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.config.JwtService;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.data.requestResponse.auth.AuthenticationResponse;
import com.example.demo.data.requestResponse.auth.LoginResponse;
import com.example.demo.data.requestResponse.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.cloudinary.CLOUDINARY_URL}")
    private String CLOUDINARY_URL;
    public BalanceResponse balanceAdd(BalanceRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(user.getBalance() == null){
            user.setBalance(request.getAmount());
        }
        else {
            user.setBalance(user.getBalance() + request.getAmount());
        }
        repository.save(user);
        return BalanceResponse.builder()
                .balance(user.getBalance())
                .message("Balance added")
                .build();
    }

    public AuthenticationResponse changeUsername(ChangeUsernameRequest request, String token) {
        System.out.println(jwtService.extractId(token));
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(repository.existsByUsername(request.getUsername())){
            return AuthenticationResponse.builder()
                    .error("Username already exists")
                    .build();
        }
        user.setUsername(request.getUsername());
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Username changed!")
                .token(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Password changed!")
                .build();
    }

    public AuthenticationResponse deleteAccount(ChangePasswordRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        repository.delete(user);
        return AuthenticationResponse.builder()
                .message("Account deleted!")
                .build();
    }

    public LoginResponse refresh(String token) {
        try{
            var user = repository.findById(jwtService.extractId(token)).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return LoginResponse.builder()
                    .token(jwtToken)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .balance(user.getBalance())
                    .role(user.getRole().name())
                    .pictureUrl(user.getPictureUrl())
                    .favorites(user.getFavorites())
                    .balance(user.getBalance())
                    .exchanges(user.getExchanges())
                    .build();
        }
        catch (Exception e){
            return LoginResponse.builder()
                    .error("Invalid token")
                    .build();
        }
    }

    public ImageResponse uploadImage(MultipartFile image, String token) {
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        System.out.println(CLOUDINARY_URL);
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        try {
            if(user.getPictureUrl() != null){
                var publicId = user.getPictureUrl().substring(user.getPictureUrl().lastIndexOf("/") + 1, user.getPictureUrl().lastIndexOf("."));
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            user.setPictureUrl(uploadResult.get("url").toString());
            repository.save(user);
            return ImageResponse.builder()
                    .message("Image uploaded")
                    .pictureUrl(uploadResult.get("url").toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ImageResponse.builder()
                    .error("Image upload failed")
                    .build();
        }
    }

    public ImageResponse deleteImage(String token) {
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        try {
            if(user.getPictureUrl() != null){
                var publicId = user.getPictureUrl().substring(user.getPictureUrl().lastIndexOf("/") + 1, user.getPictureUrl().lastIndexOf("."));
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                user.setPictureUrl(null);
                repository.save(user);
                return ImageResponse.builder()
                        .message("Image deleted")
                        .build();
            }
            return ImageResponse.builder()
                    .error("No image to delete")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ImageResponse.builder()
                    .error("Image delete failed")
                    .build();
        }
    }
}
