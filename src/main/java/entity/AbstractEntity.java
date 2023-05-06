package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public abstract class AbstractEntity {
    @Id
    private int id;
}
