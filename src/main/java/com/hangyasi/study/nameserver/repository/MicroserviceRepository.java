package com.hangyasi.study.nameserver.repository;

import com.hangyasi.study.nameserver.entity.Microservice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository, mely alap CRUD, valamint ezek hibernate dialektussal kibővített, tranzakcióban
 * futó metódusait adja.
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
@Repository
public interface MicroserviceRepository extends JpaRepository<Microservice, Long> {

  Optional<Microservice> findByNameAndIpAddressAndPort(String name, String ipAddress, int port);
  List<Microservice> findByName(String name);
}
