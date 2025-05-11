package com.siemens.internship.service;

import com.siemens.internship.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//Comment: added item service interface for extra layer of abstraction and  a better dependency handling (injection) with the ItemController
public interface ItemService {

    public List<Item> findAll();
    public Optional<Item> findById(Long id);
    public Item save(Item item);
    public void deleteById(Long id);
    public List<Long> findAllIds();

    public CompletableFuture<List<Item>> processItemsAsync();
}
