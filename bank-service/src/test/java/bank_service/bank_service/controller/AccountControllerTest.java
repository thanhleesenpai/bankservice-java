//package bank_service.bank_service.controller;
//
//import bank_service.bank_service.model.Account;
//import bank_service.bank_service.service.AccountService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountControllerTest {
//
//    @Mock
//    private AccountService accountService;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private AccountController accountController;
//
//    // Test Case 1: Admin có thể lấy thông tin tài khoản của người khác thành công.
//    @Test
//    void getAccountById_AsAdmin_ReturnsAccount() {
//        // Mock dữ liệu
//        String accountId = "user123";
//        Account mockAccount = new Account();
//        mockAccount.setAccountId(accountId);
//
//        // Giả lập quyền Admin cho đối tượng Authentication
//        when(authentication.getName()).thenReturn("adminId");
//        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
//
//        // Giả lập AccountService trả về tài khoản
//        when(accountService.getAccountById(accountId)).thenReturn(Optional.of(mockAccount));
//
//        // Thực thi phương thức cần kiểm tra
//        ResponseEntity<?> response = accountController.get(accountId, authentication);
//
//        // Khẳng định kết quả
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockAccount, response.getBody());
//        verify(accountService, times(1)).getAccountById(accountId);
//    }
//
//    // Test Case 2: Người dùng thường không thể xem tài khoản của người khác.
//    @Test
//    void getAccountById_AsUser_CannotViewOtherAccount() {
//        // Mock dữ liệu
//        String requesterId = "user123";
//        String targetId = "otherUser456";
//
//        // Giả lập quyền User thường cho đối tượng Authentication
//        when(authentication.getName()).thenReturn(requesterId);
//        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
//
//        // Thực thi phương thức cần kiểm tra
//        ResponseEntity<?> response = accountController.get(targetId, authentication);
//
//        // Khẳng định kết quả
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertEquals("You are not allowed to view this account.", response.getBody());
//        // Đảm bảo rằng AccountService.getAccountById không được gọi
//        verify(accountService, never()).getAccountById(anyString());
//    }
//    // Test Case 3: Tạo tài khoản thành công.
//    @Test
//    void createAccount_Success_ReturnsCreatedAccount() {
//        // Mock dữ liệu đầu vào và đầu ra
//        Account inputAccount = new Account();
//        inputAccount.setCustomerName("Test User");
//        Account createdAccount = new Account();
//        createdAccount.setAccountId("new-id");
//
//        // Giả lập AccountService trả về tài khoản đã tạo
//        when(accountService.createAccount(inputAccount)).thenReturn(createdAccount);
//
//        // Thực thi phương thức cần kiểm tra
//        Account result = accountController.create(inputAccount);
//
//        // Khẳng định kết quả
//        assertEquals(createdAccount, result);
//        verify(accountService, times(1)).createAccount(inputAccount);
//    }
//    // Test Case 4: Lấy danh sách tất cả tài khoản thành công (dành cho Admin).
//    @Test
//    void getAllAccounts_Success_ReturnsAllAccountsList() {
//        // Mock dữ liệu
//        List<Account> mockAccountList = List.of(new Account(), new Account());
//
//        // Giả lập AccountService trả về danh sách
//        when(accountService.getAllAccounts()).thenReturn(mockAccountList);
//
//        // Thực thi phương thức cần kiểm tra
//        ResponseEntity<List<Account>> response = accountController.getAllAccounts();
//
//        // Khẳng định kết quả
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockAccountList, response.getBody());
//        verify(accountService, times(1)).getAllAccounts();
//    }
//    // Test Case 5: Cập nhật tài khoản thành công.
//    @Test
//    void updateAccount_Success_ReturnsUpdatedAccount() {
//        // Mock dữ liệu
//        String accountId = "test-id";
//        Account updatedAccountData = new Account();
//        updatedAccountData.setCustomerName("New Name");
//        Account updatedAccount = new Account();
//        updatedAccount.setAccountId(accountId);
//        updatedAccount.setCustomerName("New Name");
//
//        // Giả lập AccountService
//        when(accountService.updateAccount(
//                eq(accountId),
//                anyString(),
//                anyString(),
//                anyString(),
//                any()
//        )).thenReturn(updatedAccount);
//
//        // Thực thi phương thức cần kiểm tra
//        Account result = accountController.update(accountId, updatedAccountData);
//
//        // Khẳng định kết quả
//        assertEquals(updatedAccount, result);
//        verify(accountService, times(1)).updateAccount(
//                eq(accountId),
//                anyString(),
//                anyString(),
//                anyString(),
//                any()
//        );
//    }
//    @Test
//    void updateAccount_NotFound_ReturnsNull() {
//        // Mock dữ liệu đầu vào
//        String accountId = "non-existent-id";
//
//        // Tạo đối tượng updatedAccountData với các giá trị cụ thể
//        // để đảm bảo khớp với các Argument Matcher của Mockito
//        Account updatedAccountData = new Account();
//        updatedAccountData.setCustomerName("New Name");
//        updatedAccountData.setEmail("new.email@example.com");
//
//        // Giả lập accountService trả về null khi không tìm thấy tài khoản.
//        when(accountService.updateAccount(
//                eq(accountId),
//                anyString(),
//                anyString(),
//                anyString(),
//                any()
//        )).thenReturn(null);
//
//        // Thực thi phương thức cần kiểm tra
//        Account result = accountController.update(accountId, updatedAccountData);
//
//        // Khẳng định kết quả
//        assertNull(result);
//
//        // Xác minh phương thức đã được gọi
//        verify(accountService, times(1)).updateAccount(
//                eq(accountId),
//                anyString(),
//                anyString(),
//                anyString(),
//                any()
//        );
//    }
//    // Test Case 6: Xóa tài khoản thành công (Admin xóa của người khác).
//    @Test
//    void deleteAccount_AsAdmin_ReturnsOk() {
//        // Mock dữ liệu
//        String accountIdToDelete = "user123";
//
//        // Giả lập Authentication là Admin
//        when(authentication.getName()).thenReturn("adminId");
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
//
//        // Thực thi phương thức cần kiểm tra
//        ResponseEntity<?> response = accountController.deleteAccount(accountIdToDelete, authentication);
//
//        // Khẳng định kết quả
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Account deleted successfully.", response.getBody());
//        verify(accountService, times(1)).deleteAccount(accountIdToDelete);
//    }
//
//    // Test Case 7: Xóa tài khoản thất bại (User xóa của người khác).
//    @Test
//    void deleteAccount_AsUser_CannotDeleteOtherAccount() {
//        // Mock dữ liệu
//        String requesterId = "user123";
//        String targetIdToDelete = "otherUser456";
//
//        // Giả lập Authentication là User thường
//        when(authentication.getName()).thenReturn(requesterId);
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
//
//        // Thực thi phương thức cần kiểm tra
//        ResponseEntity<?> response = accountController.deleteAccount(targetIdToDelete, authentication);
//
//        // Khẳng định kết quả
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertEquals("You are not allowed to delete this account.", response.getBody());
//        verify(accountService, never()).deleteAccount(anyString());
//    }
//}
package bank_service.bank_service.controller;

import bank_service.bank_service.model.Account;
import bank_service.bank_service.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountController accountController;

    // --- GET API Tests ---

    @Test
    void getAccountById_AsAdmin_ReturnsAccount() {
        String accountId = "user123";
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        when(authentication.getName()).thenReturn("adminId");
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(accountService.getAccountById(accountId)).thenReturn(Optional.of(mockAccount));
        ResponseEntity<?> response = accountController.get(accountId, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccount, response.getBody());
        verify(accountService, times(1)).getAccountById(accountId);
    }

    @Test
    void getAccountById_AsUser_CannotViewOtherAccount() {
        String requesterId = "user123";
        String targetId = "otherUser456";
        when(authentication.getName()).thenReturn(requesterId);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        ResponseEntity<?> response = accountController.get(targetId, authentication);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not allowed to view this account.", response.getBody());
        verify(accountService, never()).getAccountById(anyString());
    }

    // --- CREATE API Tests ---

    @Test
    void createAccount_Success_ReturnsCreatedAccount() {
        Account inputAccount = new Account();
        inputAccount.setCustomerName("Test User");
        Account createdAccount = new Account();
        createdAccount.setAccountId("new-id");
        when(accountService.createAccount(any(Account.class))).thenReturn(createdAccount);
        Account result = accountController.create(inputAccount);
        assertEquals(createdAccount, result);
        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    // --- GET ALL API Tests ---

    @Test
    void getAllAccounts_Success_ReturnsAllAccountsList() {
        List<Account> mockAccountList = List.of(new Account(), new Account());
        when(accountService.getAllAccounts()).thenReturn(mockAccountList);
        ResponseEntity<List<Account>> response = accountController.getAllAccounts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountList, response.getBody());
        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void getAllAccounts_ReturnsEmptyList() {
        List<Account> mockAccountList = Collections.emptyList();
        when(accountService.getAllAccounts()).thenReturn(mockAccountList);
        ResponseEntity<List<Account>> response = accountController.getAllAccounts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountList, response.getBody());
        verify(accountService, times(1)).getAllAccounts();
    }

    // --- UPDATE API Tests ---

    @Test
    void updateAccount_Success_ReturnsUpdatedAccount() {
        String accountId = "test-id";
        Account updatedAccountData = new Account();
        updatedAccountData.setCustomerName("New Name");
        updatedAccountData.setEmail("test@email.com");
        Account updatedAccount = new Account();
        updatedAccount.setAccountId(accountId);
        updatedAccount.setCustomerName("New Name");

        lenient().when(accountService.updateAccount(eq(accountId), nullable(String.class), nullable(String.class), nullable(String.class), any())).thenReturn(updatedAccount);

        Account result = accountController.update(accountId, updatedAccountData);

        assertEquals(updatedAccount, result);
        verify(accountService, times(1)).updateAccount(eq(accountId), nullable(String.class), nullable(String.class), nullable(String.class), any());
    }

    @Test
    void updateAccount_NotFound_ReturnsNull() {
        String accountId = "non-existent-id";
        Account updatedAccountData = new Account();

        lenient().when(accountService.updateAccount(eq(accountId), nullable(String.class), nullable(String.class), nullable(String.class), any())).thenReturn(null);

        Account result = accountController.update(accountId, updatedAccountData);

        assertNull(result);
        verify(accountService, times(1)).updateAccount(eq(accountId), nullable(String.class), nullable(String.class), nullable(String.class), any());
    }

    // --- DELETE API Tests ---

    @Test
    void deleteAccount_AsAdmin_ReturnsOk() throws Exception {
        String accountIdToDelete = "user123";
        when(authentication.getName()).thenReturn("adminId");
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        doNothing().when(accountService).deleteAccount(accountIdToDelete);
        ResponseEntity<?> response = accountController.deleteAccount(accountIdToDelete, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account deleted successfully.", response.getBody());
        verify(accountService, times(1)).deleteAccount(accountIdToDelete);
    }

    @Test
    void deleteAccount_AsUser_CannotDeleteOtherAccount() {
        String requesterId = "user123";
        String targetIdToDelete = "otherUser456";
        when(authentication.getName()).thenReturn(requesterId);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        ResponseEntity<?> response = accountController.deleteAccount(targetIdToDelete, authentication);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not allowed to delete this account.", response.getBody());
        verify(accountService, never()).deleteAccount(anyString());
    }
}