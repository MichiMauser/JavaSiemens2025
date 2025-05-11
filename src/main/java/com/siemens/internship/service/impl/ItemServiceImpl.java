package com.siemens.internship.service.impl;

import com.siemens.internship.repository.ItemRepository;
import com.siemens.internship.model.Item;
import com.siemens.internship.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final List<Item> processedItems = new ArrayList<>();
    private int processedCount = 0;


    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
    // Comment: added findAllIds for extracting all the ids only
    public List<Long> findAllIds() {
        return itemRepository.findAllIds();
    }


    /**
     * Your Tasks
     * Identify all concurrency and asynchronous programming issues in the code
     * Fix the implementation to ensure:
     * All items are properly processed before the CompletableFuture completes
     * Thread safety for all shared state
     * Proper error handling and propagation
     * Efficient use of system resources
     * Correct use of Spring's @Async annotation
     * Add appropriate comments explaining your changes and why they fix the issues
     * Write a brief explanation of what was wrong with the original implementation
     * <p>
     * Hints
     * Consider how CompletableFuture composition can help coordinate multiple async operations
     * Think about appropriate thread-safe collections
     * Examine how errors are handled and propagated
     * Consider the interaction between Spring's @Async and CompletableFuture
     */
    //  1.In the original code snippet the handling of the asynchronous functions were incorrect because the tasks wouldn't wait after the others so the processedItem list could be empty
    //  2.The processedItems function was not thread safe
    // Comment: The return type of @Async function is CompletableFuture which as of now return the list of items
    // The @Async is not explicitly needed because the .runAsync is used in our case, this tells the compiler the processItemsAsync is an async function
    public CompletableFuture<List<Item>> processItemsAsync() {

        List<Long> itemIds = itemRepository.findAllIds();

        //Comment: 	Ensures thread-safe adding to the list from multiple async threads.
        List<Item> processedItems = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Long id : itemIds) {
            //Comment: changed runAsync into the supplyAsync to fetch the data after the async function is done
            CompletableFuture<Void>  future_item = CompletableFuture.supplyAsync(() -> {
                try {
                    //Comment: the sleep is not needed if the correct modifiers are used for the asynchronous usage

                    //Comment: changed the item to optional for a better usage of jpaRepository methods and less logic for validation
                    Optional<Item> opt_item = itemRepository.findById(id);
                    if (opt_item.isEmpty()) {
                        System.out.println("Item not found -> id: "+ itemIds);
                        return null;
                    }
                    Item item =  opt_item.get();
                    item.setStatus("Processing");
                    itemRepository.save(item);
                    return item;
                } catch (Exception e) {
                    System.out.println("Error for the item nr: "+ id+" " + e.getMessage());
                }
                return null;
            }, executor).thenAcceptAsync((item) -> {
                if(item != null) {
                    processedItems.add(item);
                    System.out.println("Item saved with id: "+ item.getId());
                }
            });
            // Comment: added every async process into a list of CompletableFuture
            futures.add(future_item);
        }


        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0 ]))
                .thenApply(v -> processedItems);
    }

}

