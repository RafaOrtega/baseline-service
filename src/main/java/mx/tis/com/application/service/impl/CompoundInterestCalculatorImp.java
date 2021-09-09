/* 
* This program is free software: you can redistribute it and/or modify  
* it under the terms of the GNU General Public License as published by  
* the Free Software Foundation, version 3.
*
* This program is distributed in the hope that it will be useful, but 
* WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
* General Public License for more details.
*
* Nombre de archivo: CompoundInterestCalculatorImp.java
* Autor: raforteg
* Fecha de creación: 8 sep 2021
*/
package mx.tis.com.application.service.impl;

import java.util.ArrayList;
import mx.tis.com.application.dto.InitialInvestmentDto;
import mx.tis.com.application.dto.InvestmentYieldDto;
import mx.tis.com.application.service.CompoundInterestCalculator;

public class CompoundInterestCalculatorImp implements CompoundInterestCalculator {

  Double initialBalance;
  Double finalAmount;
  Double gainPerInvestment;
  
  /** The sum. */
  Double sum = 0.0;

  @Override
  public ArrayList<InvestmentYieldDto> createRevenueGrid(InitialInvestmentDto initialInvestment) {
    ArrayList<InvestmentYieldDto> investmentYieldDtoArray = new ArrayList<>();

    for (int index = 0; index < initialInvestment.getInvestmentYears(); index++) {

      InvestmentYieldDto investmentYieldDto = new InvestmentYieldDto();
      investmentYieldDto.setInvestmentYear(index + 1);


      if (index == 0) {
        investmentYieldDto.setYearlyInput(initialInvestment.getYearlyInput());
        initialBalance = initialInvestment.getInitialInvestment();
        investmentYieldDto.setInitialInvestment(initialBalance);
      }

      else if (index > 0) {
        investmentYieldDto.setYearlyInput((investmentYieldDtoArray.get(index - 1).getYearlyInput())
            * (1 + ((double) initialInvestment.getYearlyInputIncrement() / 100)));
        initialBalance = investmentYieldDtoArray.get(index - 1).getFinalBalance();
      }

      investmentYieldDto.setInvestmentYield((initialBalance + investmentYieldDto.getYearlyInput())
          * (initialInvestment.getInvestmentYield() / 100));

      investmentYieldDto.setFinalBalance(initialBalance + investmentYieldDto.getYearlyInput()
          + investmentYieldDto.getInvestmentYield());

      investmentYieldDto.setInitialInvestment(initialBalance);

      investmentYieldDtoArray.add(investmentYieldDto);

      sum = sum + investmentYieldDto.getInvestmentYield();

    }
    System.out.println("Ganancia por inversion \n" + sum);
    finalAmount = investmentYieldDtoArray.get(investmentYieldDtoArray.size() - 1).getFinalBalance();
    System.out.println("Monto Final\n" + finalAmount);
    return investmentYieldDtoArray;
  }


  @Override
  public boolean validateInput(InitialInvestmentDto input) {

    this.setDefaults(input);
    boolean cumple = true;

    cumple = cumple && (input.getInitialInvestment() >= 1000);
    cumple = cumple && (input.getYearlyInput() >= 0.0);
    cumple = cumple && (input.getYearlyInputIncrement() >= 0);
    cumple = cumple && (input.getInvestmentYears() >= 0.0);
    cumple = cumple && (input.getInvestmentYield() >= 0.0);

    return cumple;
  }

  private void setDefaults(InitialInvestmentDto initialInvestment) {
    Double yearlyInput = initialInvestment.getYearlyInput();
    yearlyInput = yearlyInput == null ? 0.0 : yearlyInput;
    initialInvestment.setYearlyInput(yearlyInput);

    Integer yearlyInputIncrement = initialInvestment.getYearlyInputIncrement();
    yearlyInputIncrement = yearlyInputIncrement == null ? 0 : yearlyInputIncrement;
    initialInvestment.setYearlyInputIncrement(yearlyInputIncrement);
  }

}
