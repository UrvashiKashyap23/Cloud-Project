package com.example.E_Commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ECommerceApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void signupApiTest() throws Exception {

		mockMvc.perform(post("/v1/auth/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                        {
                            "username": "cloudtest123",
                            "fullName": "Cloud Test User",
                            "email": "cloudtest123@gmail.com",
                            "phoneNumber": "9876543211",
                            "password": "Test@123",
                            "gender": "MALE"
                        }
                        """))
				.andExpect(status().isOk());
	}

	@Test
	void loginApiTest() throws Exception {

		mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                        {
                            "username": "testuser123",
                            "password": "Test@123"
                        }
                        """))
				.andExpect(status().isOk());
	}

	@Test
	void getUserProfileApiTest() throws Exception {

		mockMvc.perform(get("/v1/auth/get/profile/1"))
				.andExpect(status().isOk());
	}

	@Test
	void updateUserProfileApiTest() throws Exception {

		mockMvc.perform(put("/v1/auth/update/profile/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                        {
                            "name": "Updated Test User",
                            "username": "testuser123"
                        }
                        """))
				.andExpect(status().isOk());
	}
}