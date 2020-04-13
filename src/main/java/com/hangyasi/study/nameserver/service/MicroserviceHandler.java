package com.hangyasi.study.nameserver.service;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.util.Comparator.*;

import com.hangyasi.study.nameserver.controller.ServiceDto;
import com.hangyasi.study.nameserver.entity.Microservice;
import com.hangyasi.study.nameserver.repository.MicroserviceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * A MicroserviceHandler osztály egy spring által kezelt singleton bean, melynek célja az
 * adatbázisban tárolt {@link Microservice} entitások {@link MicroserviceRepository}-n keresztüli
 * módosítása, mentése, törlése.
 *
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
@Service
public class MicroserviceHandler {

  private MicroserviceRepository repository;

  private static final String PORT_DELIMITER = ":";

  /**
   * A deleteOldServices metódus törli azon metódusokat, melyek legalább két perce nem
   * küldtek magukról életjelet. Meghívását a spring végzi a @Scheduled annotáción keresztül, 
   * fixen két percenként.
   */
  @Scheduled(fixedRate = 120000)
  public void deleteOldServices() {
    LocalDateTime now = LocalDateTime.now();
    List<Microservice> services = repository.findAll();
    List<Microservice> toDelete = services.stream()
        .filter(service -> isDeletable(now, service))
        .collect(Collectors.toList());
    if (toDelete != null && !toDelete.isEmpty()) {
      repository.deleteAll(toDelete);
    }
  }

  private boolean isDeletable(LocalDateTime now, Microservice service) {
    return service.getLastHeartBeat().isBefore(now.minusMinutes(2));
  }

  /**
   * Az osztály egyetlen publikus konstruktora, melybe a spring fogja beinjektálni
   * az egyetéen tagváltozót, a {@link MicroserviceRepository}-t.
   * @param repository egy JPA-s interface, mely hibernate dialektussal kiegészített CRUD
   * műveleteket tartalmaz.
   */
  public MicroserviceHandler(MicroserviceRepository repository) {
    this.repository = repository;
  }

  /**
   * A getLeastLoadedServiceUrl visszaadja a kért serviceek közül azt, melyet a legrégebben
   * kértek el a szervertől. Ezzel a metódus egy egyszerű load balancer implementációnak tekinthető.
   * Visszaadás után frissíti a service lastReturned  adattagját az aktuális LocalDateTime.now()
   * használatával.
   * @param serviceName a kért microservice neve.
   * @return egy Optional-t ad vissza, benne a service URL-lel, ha létezett, vagy üresen, ha nem.
   */
  public Optional<String> getLeastLoadedServiceUrl(String serviceName) {
    List<Microservice> services = repository.findByName(serviceName);
    if (Objects.isNull(services) || services.isEmpty()) {
      return Optional.empty();
    } else {
      Microservice result = getLoadBalancedService(services);
      updateLastReturned(result);
      return Optional.of(join(PORT_DELIMITER, result.getIpAddress(), valueOf(result.getPort())));
    }
  }

  private void updateLastReturned(Microservice service) {
    service.setLastReturned(LocalDateTime.now());
    save(service);
  }

  private Microservice getLoadBalancedService(List<Microservice> services) {
    return services.stream()
            .sorted(nullsFirst(comparing(Microservice::getLastReturned)))
            .findFirst().get();
  }

  private void save(Microservice service) {
    repository.save(service);
  }

  /**
   * A sendHeartBeat metódus regisztrálja be az új Microservice-eket, illetve frissíti a már meglévők
   * lastHeartBeat adattagját a LocalDateTime.now() használatával.
   * @param dto a REST-en érkezett dto, melyből létrehozzuk, vagy frissítjük a {@link Microservice}
   * entitásunkat.
   */
  public void sendHeartBeat(ServiceDto dto) {
    Optional<Microservice> maybeService =
        repository.findByNameAndIpAddressAndPort(dto.getName(), dto.getIpAddress(), dto.getPort());
    Microservice service;
    if(maybeService.isPresent()) {
      service = maybeService.get();
    } else {
      service = new Microservice();
      service.setName(dto.getName());
      service.setIpAddress(dto.getIpAddress());
      service.setPort(dto.getPort());
    }
    service.setLastHeartBeat(LocalDateTime.now());
    save(service);
  }
}
