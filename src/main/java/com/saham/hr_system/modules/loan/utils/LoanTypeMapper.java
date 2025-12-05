package com.saham.hr_system.modules.loan.utils;

import org.springframework.stereotype.Component;

@Component
public class LoanTypeMapper {

    public String mapLoanType(String type){

        return switch(type){
            case "NORMAL" -> "Prêt";
            case "ADVANCE" -> "Avance sur salaire";
            default -> "Inconnu";
        };
    }
}
