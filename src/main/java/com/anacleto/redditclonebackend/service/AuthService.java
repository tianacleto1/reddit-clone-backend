package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.config.security.JwtProvider;
import com.anacleto.redditclonebackend.exception.FailToSendEmailException;
import com.anacleto.redditclonebackend.model.NotificationEmail;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.VerificationToken;
import com.anacleto.redditclonebackend.model.dto.AuthenticationResponseDTO;
import com.anacleto.redditclonebackend.model.dto.LoginRequestDTO;
import com.anacleto.redditclonebackend.model.dto.RegisterRequestDTO;
import com.anacleto.redditclonebackend.repository.UserRepository;
import com.anacleto.redditclonebackend.repository.VerificationTokenRepository;
import com.anacleto.redditclonebackend.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilderService mailContentBuilder;
    private final MailService mailService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository,
                                                    MailContentBuilderService mailContentBuilder, MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    @Transactional
    public void signup(RegisterRequestDTO registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        log.info("User Registered Successfully, Sending Authentication Email");
        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
                + Constants.ACTIVATION_EMAIL + "/" + token);

        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager
                                            .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                                            loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken = jwtProvider.generateToken(authenticate);

        return new AuthenticationResponseDTO(authenticationToken, loginRequestDTO.getUsername());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new FailToSendEmailException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new FailToSendEmailException("User Not Found with id - " + username));
        user.setEnabled(true);

        userRepository.save(user);
    }
}
