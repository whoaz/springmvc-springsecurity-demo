package com.yida.sample.controller;

import com.yida.sample.model.User;
import com.yida.sample.model.UserProfile;
import com.yida.sample.service.UserProfileService;
import com.yida.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	private UserService userService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private AuthenticationTrustResolver authenticationTrustResolver;

	@Autowired
	private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;


	@GetMapping(value = {"/", "/list"})
	public String listUsers(ModelMap modelMap) {
		List<User> users = userService.findAllUsers();
		modelMap.put("users", users);
		modelMap.put("loggedinuser", getPrincipal());
		return "list";
	}

	@GetMapping("/newUser")
	public String newUser(ModelMap modelMap) {
		User user = new User();
		modelMap.put("user", user);
		modelMap.put("edit", false);
		modelMap.put("loggedinuser", getPrincipal());
		return "registration";
	}


	@PostMapping("/newUser")
	public String saveUser(@Valid User user, BindingResult result, ModelMap modelMap) {
		if (result.hasErrors()) {
			return "registration";
		}

		if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {
			FieldError ssoError = new FieldError("user", "ssoId",
					messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
			result.addError(ssoError);
			return "registration";
		}

		userService.saveUser(user);
		modelMap.put("success", "User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
		modelMap.put("loggedinuser", getPrincipal());

		return "registrationsuccess";
	}


	@GetMapping("/edit-user-{ssoId}")
	public String editUser(@PathVariable String ssoId, ModelMap modelMap) {
		User user = userService.findBySSO(ssoId);
		modelMap.put("user", user);
		modelMap.put("edit", true);
		modelMap.put("loggedinuser", getPrincipal());
		return "registration";
	}

	@PostMapping("/edit-user-{ssoId}")
	public String updateUser(@PathVariable String ssoId, @Valid User user,
							 BindingResult result, ModelMap modelMap) {
		if (result.hasErrors()) {
			return "registration";
		}
		userService.updateUser(user);
		modelMap.put("success", "User " + user.getFirstName() + " " + user.getLastName() + " updated successfully");
		modelMap.put("loggedinuser", getPrincipal());
		return "registrationsuccess";
	}


	@GetMapping("/delete-user-{ssoId}")
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}

	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}

	@GetMapping("Access_Denied")
	public String accessDeniedPage(ModelMap modelMap) {
		modelMap.put("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	@GetMapping("/login")
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		}
		return "redirect:/list";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(authentication)) {
			persistentTokenBasedRememberMeServices.logout(request, response, authentication);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	private boolean isCurrentAuthenticationAnonymous() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}

	private String getPrincipal() {
		String userName = null;

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}
