package dev.ime.controller;


import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.exceptionhandler.GlobalExceptionHandler;


@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;	

	@MockitoBean
	private GlobalExceptionHandler globalExceptionHandler;
	
	@MockitoBean
	private Logger logger;
	
	@Test
	void MainController_index_ReturnView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/index"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("index"));
	}

	@Test
	void MainController_success_ReturnView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/success"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("success"));
	}

	@Test
	void MainController_login_ReturnView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/login"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("login"));
	}
}
