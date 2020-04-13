package com.hangyasi.study.nameserver.controller;

import com.hangyasi.study.nameserver.entity.Microservice;
import com.hangyasi.study.nameserver.service.MicroserviceHandler;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A NameServerRestController osztály egy spring által kezelt osztály, melynek segítségével
 * regisztráljuk a REST végpontokat. Ezeket a végpontokat érhetik el a kliensek.
 * A RequestMapping annotáció segítségével definiáljuk a 'services' URL részt.
 *
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
@RestController
@RequestMapping("services")
public class NameServerRestController {

  private MicroserviceHandler handler;

  public NameServerRestController(MicroserviceHandler handler) {
    this.handler = handler;
  }

  /**
   * A getServiceUrl egy HTTP GET kérést vár a microservice nevével, és visszaadja annak URL-jét
   * JSON formátumban.
   * @param serviceName a kért microservice neve path variable-ként megadva az URL-ben.
   * @return a kért microservice URL-je.
   */
  @GetMapping(
      value = "/{serviceName}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ServerResponse> getServiceUrl(@PathVariable String serviceName) {
    Optional<String> maybeResponse = handler.getLeastLoadedServiceUrl(serviceName);
    if (maybeResponse.isPresent()) {
      return ResponseEntity
          .ok(new ServerResponse(maybeResponse.get()));
    } else {
      return ResponseEntity
          .status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(new ServerResponse("Can't find the requested service."));
    }
  }

  /**
   * A sendHeartBeat metódus segítségével regisztrálhatunk be új, valamint megújíthatunk régi
   * {@link Microservice}-eket. HTTP POST kérést vár.
   * @param dto a microservice-t leíró dto JSON formátumban.
   */
  @PostMapping(
      value = "/heartbeat",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public void sendHeartBeat(@RequestBody ServiceDto dto) {
    handler.sendHeartBeat(dto);
  }

}
