package bank_service.bank_service.controller;

import bank_service.bank_service.dto.AuthResponse;
import bank_service.bank_service.dto.LoginRequest;
import bank_service.bank_service.dto.RegisterRequest;
import bank_service.bank_service.exception.AppException;
import bank_service.bank_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_Success_ReturnsToken() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPhoneNumber("0123456789");
        request.setCustomerName("Test User");
        request.setPassword("password");

        AuthResponse mockResponse = AuthResponse.builder().token("mock-token").build();
        when(authService.register(request)).thenReturn(mockResponse);

        // Act
        AuthResponse response = authController.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        verify(authService, times(1)).register(request);
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("exists@example.com");

        when(authService.register(request))
                .thenThrow(new AppException("Email is already in use"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class,
                () -> authController.register(request));

        assertEquals("Email is already in use", exception.getMessage());
        verify(authService, times(1)).register(request);
    }

    @Test
    void login_Success_ReturnsToken() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        AuthResponse mockResponse = AuthResponse.builder().token("mock-token").build();
        when(authService.login(request)).thenReturn(mockResponse);

        // Act
        AuthResponse response = authController.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        verify(authService, times(1)).login(request);
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpass");

        when(authService.login(request))
                .thenThrow(new AppException("Incorrect Account or password"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class,
                () -> authController.login(request));

        assertEquals("Incorrect Account or password", exception.getMessage());
        verify(authService, times(1)).login(request);
    }
}
