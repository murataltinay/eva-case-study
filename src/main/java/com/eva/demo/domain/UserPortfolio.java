package com.eva.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(indexes = @Index(columnList = "shareName"))
public class UserPortfolio extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(length = 3)
    private String shareName;

    private Integer quantity;

}
