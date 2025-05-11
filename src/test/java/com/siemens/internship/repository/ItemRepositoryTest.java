package com.siemens.internship.repository;

import com.siemens.internship.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveAndFindById() {
        Item item = new Item(null, "Test", "Desc", "New", "email@ex.com");
        Item saved = itemRepository.save(item);
        Optional<Item> found = itemRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }

    @Test
    void testFindAllIds() {
        itemRepository.save(new Item(null, "1", "d", "New", "a@a.com"));
        itemRepository.save(new Item(null, "2", "d", "New", "b@b.com"));

        List<Long> ids = itemRepository.findAllIds();
        assertTrue(ids.size() >= 2);
    }
}
