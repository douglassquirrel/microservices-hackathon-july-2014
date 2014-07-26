package com.microserviceshack.repository;

import com.microserviceshack.model.Fact;

import java.util.List;

/**
 * Created by paul on 26/07/2014.
 */
public interface FactRepository {

    List<Fact> find(String topic);
}
