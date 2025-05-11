package com.siemens.internship.service.impl;

import com.siemens.internship.model.Item;
import com.siemens.internship.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//Comment: I tested the service by adding an item with the wrong name "test" It will fail the test where the item is named "Test"
// I used mockito here because I need to test the mock repository for the current item service
@SpringBootTest
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testItem = new Item(1L, "test", "Tesing description", "null", "test@test.com");
    }

    @Test
    void testFindAll() {
        when(itemRepository.findAll()).thenReturn(List.of(testItem));
        List<Item> items = itemService.findAll();
        assertEquals(1, items.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        Optional<Item> item = itemService.findById(1L);
        assertTrue(item.isPresent());
        assertEquals("Test", item.get().getName());
    }

    @Test
    void testSave() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        Item saved = itemService.save(testItem);
        assertEquals("Test", saved.getName());
    }

    @Test
    void testDeleteById() {
        itemService.deleteById(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllIds() {
        when(itemRepository.findAllIds()).thenReturn(List.of(1L, 2L));
        List<Long> ids = itemService.findAllIds();
        assertEquals(2, ids.size());
    }
}
