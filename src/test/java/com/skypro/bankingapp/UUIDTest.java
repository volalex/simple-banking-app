package com.skypro.bankingapp;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UUIDTest {
  @Test
  void simpleUUIDTest(){
    UUID uuid = UUID.randomUUID();

    System.out.println(uuid);
  }
}
