package com.icia.sdsd.dto;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="SDOPERATOR")
public class OperatorEntity {

    @Id
    @Column
    private String soNum;

    @Column
    private String soName;

    @Column
    private String soBusnum;

    @Column
    private String soPw;

    @Column
    private String soEmail;

    @Column
    private String soPhone;

    @Column
    private String soCompany;

    @Column
    private String soComaddr;

    public static OperatorEntity toEntity(OperatorDTO operator){
        OperatorEntity entity = new OperatorEntity();

        entity.setSoNum(operator.getSoNum());
        entity.setSoName(operator.getSoName());
        entity.setSoBusnum(operator.getSoBusnum());
        entity.setSoPw(operator.getSoPw());
        entity.setSoEmail(operator.getSoEmail());
        entity.setSoPhone(operator.getSoPhone());
        entity.setSoCompany(operator.getSoCompany());
        entity.setSoComaddr(operator.getSoComaddr());

        return entity;
    }
}
