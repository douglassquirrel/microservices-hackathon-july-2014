package com.microserviceshack.repository;

import com.microserviceshack.model.Fact;
import com.microserviceshack.repository.impl.HackJdbcFactRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by paul on 26/07/2014.
 */
public class FactRepositoryTest {

    @Test
    public void testFindFacts() {
        FactRepository factRepository = new HackJdbcFactRepository();

        List<Fact> facts = factRepository.find("shipping");

        assertTrue(facts.size() > 0);
    }
}
