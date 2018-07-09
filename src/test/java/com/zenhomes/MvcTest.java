package com.zenhomes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/counter")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"1\":\"Villarriba\",\"2\":\"Villabajo\"}")));
    }

    @Test
    public void findMappingById() throws Exception {
        this.mockMvc.perform(get("/counter?id=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"1\":\"Villarriba\"}")));
    }

    @Test
    public void findMappingById2() throws Exception {
        this.mockMvc.perform(get("/counter?id=2")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"2\":\"Villabajo\"}")));
    }

    @Test
    public void findMappingById3() throws Exception {
        this.mockMvc.perform(get("/counter?id=3")).andDo(print()).andExpect(status().isNotFound());
    }
}