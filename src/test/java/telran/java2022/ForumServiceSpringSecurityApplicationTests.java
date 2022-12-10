package telran.java2022;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aspectj.apache.bcel.classfile.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.accounting.dto.UserAccountResponseDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.exceptions.UserExistsException;
import telran.java2022.accounting.dto.exceptions.UserNotFoundException;
import telran.java2022.accounting.model.UserAccount;
import telran.java2022.accounting.service.UserAccountService;
import telran.java2022.accounting.service.UserAccountServiceImpl;

@SpringBootTest
class ForumServiceSpringSecurityApplicationTests {
//	@Autowired
//	MockMvc mockMvc;
//	@Autowired
//	ObjectMapper objectMapper;
	
	@MockBean
	UserAccountRepository userAccountRepositoryTest;

	@Autowired
	UserAccountService userAccountServiceTest;
	
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;

	List<UserAccount> users = new ArrayList<>();
	UserAccount user1;
	UserAccount user2;
	UserAccount user3;
	
	@BeforeEach
	public void setUp() {
//		userAccountServiceTest = new UserAccountServiceImpl(userAccountRepositoryTest, modelMapper, passwordEncoder);
		user1 = new UserAccount("User1", "111", "John", "Smith");
		user2 = new UserAccount("User2", "222", "Mary", "Smith");
		user3 = new UserAccount("User3", "333", "Peter", "");
		users.add(user1);
		users.add(user2);
		users.add(user3);
		
//		users.stream().map(u -> modelMapper.map(u, UserRegisterDto.class)).forEach(u -> userAccountServiceTest.addUser(u));

	}
	
	@Test
	public void addUser() throws Exception {
		UserAccount user = new UserAccount("User1", "111", "John", "Smith");
	    UserAccount expected = new UserAccount("User1", "111", "John", "Smith");
		
		UserRegisterDto userRegisterDto = modelMapper.map(user, UserRegisterDto.class);	
		when(userAccountRepositoryTest.save(user)).thenReturn(expected);

//		Mockito.when(userAccountRepository.existsById(userRegisterDto.getLogin())).thenReturn(false);
		UserAccountResponseDto actual = userAccountServiceTest.addUser(userRegisterDto);
		Assertions.assertEquals(expected.getLogin(), actual.getLogin());
		
		
//		mockMvc.perform(post("/account/register")
//					.content(objectMapper.writeValueAsString(userAccount))
//					.contentType(MediaType.APPLICATION_JSON)
//					)
//				.andExpect(status().isOk())
//				.andExpect(content().json(objectMapper.writeValueAsString(userAccount)));
	}
	
	
	
//	@Test
//	public void addUserExists() throws Exception {
//		UserAccount userAccount = new UserAccount("User1", "1234", "John", "Smith");
//		
//		UserRegisterDto userRegisterDto = modelMapper.map(userAccount, UserRegisterDto.class);	
//		
//		Mockito.when(userAccountRepository.existsById(userAccount.getLogin())).thenReturn(true);
//		UserExistsException exception = Assertions.assertThrows(UserExistsException.class, () -> userAccountService.addUser(userRegisterDto));
////		Assertions.assertTrue(exception.getMessage().contains("User " + userAccount.getLogin() + " exists"));
////		Assertions.assertEquals("User " + userAccount.getLogin() + " exists", exception.getMessage());
//		assertThat(exception).isInstanceOf(UserExistsException.class)
//        .hasMessageContaining("User " + userAccount.getLogin() + " exists");
//	}
	
	
	@Test
	public void getUserByLogin() throws Exception {
		String login = "User4";
		when(userAccountRepositoryTest.findById(login)).thenReturn(Optional.of(user1));
		UserAccountResponseDto actual = userAccountServiceTest.getUser(login);
		UserAccountResponseDto expected = modelMapper.map(user1, UserAccountResponseDto.class);	

		
		Assertions.assertEquals(expected.getLogin(), actual.getLogin());
//		Assertions.assertThrows(UserNotFoundException.class, );
		
		assertThatObject(actual.getLogin()).isEqualTo(expected.getLogin());
		assertThatExceptionOfType(UserNotFoundException.class);
	}
		
//	Assertions.assertEquals("User " + userAccount.getLogin() + " not found", exception.getMessage());
//		Assertions.assertTrue(exception.getMessage().contains("User " + userAccount.getLogin() + " not found"));
//	}
	
	
	@Test
	public void removeUserByLogin() throws Exception {
		String login = "User3";
		when(userAccountRepositoryTest.findById(login)).thenReturn(Optional.of(user3));
		UserAccountResponseDto actual = userAccountServiceTest.removeUser(login);
		UserAccountResponseDto expected = modelMapper.map(user3, UserAccountResponseDto.class);	

		assertThatObject(actual.getLogin()).isEqualTo(expected.getLogin());
//		verify(userAccountRepositoryTest).deleteById(login);
	}

}
