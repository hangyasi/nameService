package com.hangyasi.study.nameserver.controller;

/**
 * DTO, melyet kliensek JSON kéréseiből tölt fel a szerver.
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
public class ServiceDto {

  private String name;
  private String ipAddress;
  private int port;

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
}
