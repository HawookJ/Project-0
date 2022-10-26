package com.mealkit.domain;


import com.mealkit.domain.constant.AuditingFields;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@ToString
public class Board extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;

    private Long boardNo;

}
