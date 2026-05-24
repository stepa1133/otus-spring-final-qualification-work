package ru.otus.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.db.entity.Role;
import ru.otus.db.repository.RoleRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleCache {

    private final RoleRepository roleRepository;

    private Map<String, Role> cache;

    @PostConstruct
    public void init() {
        cache = roleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Role::getName, r -> r));
    }

    public Role get(String name) {
        return cache.get(name);
    }
}
