package br.com.treinaweb.springbootapi.controller;

import java.util.SequencedCollection;
import java.util.Optional;

import br.com.treinaweb.springbootapi.entity.Pessoa;
import br.com.treinaweb.springbootapi.repository.PessoaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/// Controller for managing Pessoa entities.
/// Provides endpoints for CRUD operations on Pessoa.
@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    /// Constructor for PessoaController.
    /// @param pessoaRepository Repository for Pessoa entities
    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    /// Returns a list of all pessoas.
    /// @return List of Pessoa
    @Operation(summary = "Retorna uma lista de pessoas", security = { @SecurityRequirement(name = "BearerAuthentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de pessoa"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @GetMapping(value = "/pessoa", produces = "application/json")
    public SequencedCollection<Pessoa> getAll() {
        return pessoaRepository.findAll();
    }

    /// Returns a pessoa by its id.
    /// @param id Pessoa id
    /// @return ResponseEntity with Pessoa or 404 if not found
    @Operation(summary = "Retorna uma pessoa", security = { @SecurityRequirement(name = "BearerAuthentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de pessoa"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @GetMapping(value = "/pessoa/{id}", produces = "application/json")
    public ResponseEntity<Pessoa> getById(@PathVariable(value = "id") long id) {
        return pessoaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /// Creates a new pessoa.
    /// @param pessoa Pessoa to create
    /// @return Created Pessoa
    @Operation(summary = "Cadastra uma pessoa", security = { @SecurityRequirement(name = "BearerAuthentication") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro cadastrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não localizado"),
        @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @PostMapping(value = "/pessoa", produces = "application/json", consumes = "application/json")
    public Pessoa create(@Valid @RequestBody Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    /// Updates an existing pessoa by id.
    /// @param id Pessoa id
    /// @param newPessoa Pessoa data to update
    /// @return ResponseEntity with updated Pessoa or 404 if not found
    @Operation(summary = "Atualiza uma pessoa", security = { @SecurityRequirement(name = "BearerAuthentication") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não localizado"),
        @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @PutMapping(value = "/pessoa/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Pessoa> update(@PathVariable(value = "id") long id, @Valid @RequestBody Pessoa newPessoa) {
        return pessoaRepository.findById(id)
                .map(pessoa -> {
                    pessoa.setNome(newPessoa.getNome());
                    return ResponseEntity.ok(pessoaRepository.save(pessoa));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /// Deletes a pessoa by id.
    /// @param id Pessoa id
    /// @return ResponseEntity with status 200 if deleted, 404 if not found
    @Operation(summary = "Deleta uma pessoa", security = { @SecurityRequirement(name = "BearerAuthentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não localizado"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @DeleteMapping(value = "/pessoa/{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long id) {
        return pessoaRepository.findById(id)
                .map(pessoa -> {
                    pessoaRepository.delete(pessoa);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}