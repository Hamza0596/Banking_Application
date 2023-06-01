package com.banking.bankingapplication.listner;


        import com.banking.bankingapplication.domain.UserPrincipal;
        import com.banking.bankingapplication.service.serviceimpl.LoginAttemptService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.event.EventListener;
        import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
        import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
        import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener({AuthenticationSuccessEvent.class, InteractiveAuthenticationSuccessEvent.class})
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}