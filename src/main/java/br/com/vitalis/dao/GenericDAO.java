package br.com.vitalis.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, K> {
    T inserir(T e);
    boolean atualizar(T e);
    boolean deletar(K id);
    Optional<T> buscar(K id);
    List<T> listar();
}