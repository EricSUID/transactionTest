package com.transaction.test.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User1 {
    private Integer id;
    private String name;
}
