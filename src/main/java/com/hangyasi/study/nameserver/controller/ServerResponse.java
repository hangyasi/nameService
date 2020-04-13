package com.hangyasi.study.nameserver.controller;

/**
 * DTO, melybe a szerver által adott válaszokat csomagolhatjuk.
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
public class ServerResponse {

  private String response;

  public ServerResponse(String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }
}
