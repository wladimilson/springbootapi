package br.com.treinaweb.springbootapi.controller;

import br.com.treinaweb.springbootapi.entity.Pessoa;
import br.com.treinaweb.springbootapi.repository.PessoaRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class PessoaControllerWebTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private PessoaRepository pessoaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser
    void whenPostInvalidPessoa_thenReturns400() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Ab"); // Too short

        mockMvc.perform(post("/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void whenPostValidPessoa_thenReturns200() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Valid Name");
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        mockMvc.perform(post("/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenPutInvalidPessoa_thenReturns400() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Ab"); // Too short

        mockMvc.perform(put("/pessoa/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void whenPostLongName_thenReturns400() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("A".repeat(256)); // Too long

        mockMvc.perform(post("/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest());
    }
}
