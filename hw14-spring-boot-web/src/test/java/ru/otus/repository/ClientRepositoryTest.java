package ru.otus.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.model.Address;
import ru.otus.model.Client;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void canSaveUserWithAddress() {
        var address = new Address("street");
        var newUser = new Client("Name", address, Set.of());

        var savedUser = clientRepository.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getAddress().getAddressId()).isNotNull();

        List<Client> all = clientRepository.findAll();

        assertThat(all).hasSize(1);
    }
}