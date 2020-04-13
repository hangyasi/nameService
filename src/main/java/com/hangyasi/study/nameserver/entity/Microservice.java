package com.hangyasi.study.nameserver.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A szerver egyetlen entity-je, melyet a hibernate a kapcsolódó adatbázisra mappel, így ez
 * megfeleltethető egy adatbázis-táblának.
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
@Entity
public class Microservice {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private long id;
  private String name;
  private String ipAddress;
  private int port;
  @UpdateTimestamp
  private LocalDateTime lastHeartBeat;
  private LocalDateTime lastReturned;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public LocalDateTime getLastHeartBeat() {
    return lastHeartBeat;
  }

  public void setLastHeartBeat(LocalDateTime lastHeartBeat) {
    this.lastHeartBeat = lastHeartBeat;
  }

  public LocalDateTime getLastReturned() {
    return lastReturned;
  }

  public void setLastReturned(LocalDateTime lastReturned) {
    this.lastReturned = lastReturned;
  }

}
