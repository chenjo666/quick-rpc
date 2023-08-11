package com.cj.v9.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isBorrow;
}
