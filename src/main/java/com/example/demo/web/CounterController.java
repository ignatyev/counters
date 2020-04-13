package com.example.demo.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@RestController
public class CounterController {

    private ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

    @PostMapping
    public void createCounter(@RequestBody String name) {
        counters.putIfAbsent(name, new LongAdder());
    }

    @PostMapping("/increment")
    public void increment(@RequestBody String name) {
        counters.computeIfPresent(name, (s, longAdder) -> {
            longAdder.increment();
            return longAdder;
        });
    }

    @DeleteMapping
    public void delete(@RequestBody String name) {
        counters.remove(name);
    }

    @GetMapping
    public Long get(@RequestBody String name) {
        return counters.getOrDefault(name, new LongAdder()).sum();
    }

    @GetMapping("/getAll")
    public Long getAll() {
        return counters.values().stream()
                .map(LongAdder::sum)
                .mapToLong(Long::longValue)
                .sum();
    }

    @GetMapping("/getNames")
    public List<String> getNames() {
        return new ArrayList<>(counters.keySet());
    }
}
