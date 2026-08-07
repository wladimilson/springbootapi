package br.com.treinaweb.springbootapi.controller;

import br.com.treinaweb.springbootapi.entity.Pessoa;
import br.com.treinaweb.springbootapi.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.SequencedCollection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PessoaControllerTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetAll() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Teste");
        when(pessoaRepository.findAll()).thenReturn(List.of(pessoa));
        SequencedCollection<Pessoa> pessoas = pessoaController.getAll();
        assertEquals(1, pessoas.size());
        assertEquals("Teste", pessoas.getFirst().getNome());
    }

    @Test
    void testGetByIdFound() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Teste");
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        ResponseEntity<Pessoa> response = pessoaController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Teste", response.getBody().getNome());
    }

    @Test
    void testGetByIdNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Pessoa> response = pessoaController.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPost() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Teste");
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        Pessoa result = pessoaController.create(pessoa);
        assertEquals("Teste", result.getNome());
    }

    @Test
    void testPutFound() {
        Pessoa oldPessoa = new Pessoa();
        oldPessoa.setId(1L);
        oldPessoa.setNome("Old");
        Pessoa newPessoa = new Pessoa();
        newPessoa.setNome("New");
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(oldPessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(oldPessoa);
        ResponseEntity<Pessoa> response = pessoaController.update(1L, newPessoa);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New", response.getBody().getNome());
    }

    @Test
    void testPutNotFound() {
        Pessoa newPessoa = new Pessoa();
        newPessoa.setNome("New");
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Pessoa> response = pessoaController.update(1L, newPessoa);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteFound() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).delete(pessoa);
        ResponseEntity<Void> response = pessoaController.delete(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = pessoaController.delete(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateWithNullName() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(null);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        
        Pessoa result = pessoaController.create(pessoa);
        assertNull(result.getNome());
        verify(pessoaRepository).save(pessoa);
    }

    @Test
    void testGetAllEmpty() {
        when(pessoaRepository.findAll()).thenReturn(List.of());
        SequencedCollection<Pessoa> pessoas = pessoaController.getAll();
        assertTrue(pessoas.isEmpty());
    }

    @Test
    void testCreateWithEmptyName() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("");
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        
        Pessoa result = pessoaController.create(pessoa);
        assertEquals("", result.getNome());
        verify(pessoaRepository).save(pessoa);
    }
}
