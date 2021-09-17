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
* Fecha de creación: 10 sep 2021
*/

package com.tis.mx.application.service.impl;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.tis.mx.application.dto.InitialInvestmentDto;
import com.tis.mx.application.dto.InvestmentYieldDto;
import com.tis.mx.application.service.CompoundInterestCalculator;

/**
 * The Class CompoundInterestCalculatorImp.
 */
@Service
public class CompoundInterestCalculatorImp implements CompoundInterestCalculator {

  /** The initial balance. */
  Double initialBalance;
  
  /** The final amount. */
  Double finalAmount;

  /** The sum. */
  Double sum = 0.0;

  /**
   * Creates the revenue grid.
   *
   * @param initialInvestment the initial investment
   * @return the array list
   */
  @Override
  @HystrixCommand(commandKey = "createRevenueGrid", fallbackMethod = "fallbackRevenueGrid")
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
  
  
/**
 * Fallback revenue grid.
 *
 * @param initialInvestmentDto the initial investment dto
 * @return the array list
 */
public ArrayList<InvestmentYieldDto> fallbackRevenueGrid(InitialInvestmentDto initialInvestmentDto) {
    
    return null;
  }

  /**
   * Validate input.
   *
   * @param input the input
   * @return true, if successful
   */
  @Override
  public boolean validateInput(InitialInvestmentDto input) {

    this.setDefaults(input);

    if (input.getInitialInvestment() >= 1000) {
      if (input.getYearlyInput() >= 0.0) {
        if (input.getYearlyInputIncrement() >= 0) {
          if (input.getInvestmentYears() >= 0.0) {
            if (input.getInvestmentYield() > 0.0) {
              return true;
            }
            return false;
          }
          return false;
        }
        return false;
      }
      return false;
    }
    return false;
  }

  /**
   * Sets the defaults.
   *
   * @param initialInvestment the new defaults
   */
  private void setDefaults(InitialInvestmentDto initialInvestment) {

    if (initialInvestment.getYearlyInput() == null) {
      initialInvestment.setYearlyInput(0.0);
    } else {
      initialInvestment.setYearlyInput(initialInvestment.getYearlyInput());
    }
    if (initialInvestment.getYearlyInputIncrement() == null) {
      initialInvestment.setYearlyInputIncrement(0);
    } else {
      initialInvestment.setYearlyInputIncrement(initialInvestment.getYearlyInputIncrement());
    }
  }

}
