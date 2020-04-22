package controller;

import com.elena.elena.controller.RouteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RouteController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;
}
