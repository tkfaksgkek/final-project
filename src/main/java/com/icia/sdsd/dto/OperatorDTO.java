package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("operator")
public class OperatorDTO {
    private String soNum;       // 사업자 구분용 일련번호
    private String soName;      // 사업자 이름
    private String soBusnum;    // 사업자 등록 번호(ID)
    private String soPw;        // 사업자 비밀번호(PW)
    private String soEmail;     // 사업자 이메일
    private String soPhone;     // 사업자 핸드폰 번호
    private String soCompany;   // 사업자 회사 이름 (법인 이름)
    private String soComaddr;   // 사업자 회사 주소 (법인 주소)

    public static OperatorDTO toDTO(OperatorEntity entity){
        OperatorDTO operator = new OperatorDTO();

        operator.setSoNum(entity.getSoNum());
        operator.setSoName(entity.getSoName());
        operator.setSoBusnum(entity.getSoBusnum());
        operator.setSoPw(entity.getSoPw());
        operator.setSoEmail(entity.getSoEmail());
        operator.setSoPhone(entity.getSoPhone());
        operator.setSoCompany(entity.getSoCompany());
        operator.setSoComaddr(entity.getSoComaddr());

        return operator;
    }
}
