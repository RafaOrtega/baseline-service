/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * Nombre de archivo: ApplicationControllerTest.java Autor: raforteg Fecha de creación: 9 sep 2021
 */
package com.tis.mx.application.controller;

import mx.tis.com.application.controller.ApplicationController;
import mx.tis.com.application.dto.InitialInvestmentDto;
import mx.tis.com.application.dto.InvestmentYieldDto;
import mx.tis.com.application.service.CompoundInterestCalculator;
import mx.tis.com.application.service.impl.CompoundInterestCalculatorImp;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ApplicationControllerTest {

  private ApplicationController controller;
  private InitialInvestmentDto initialInvestment;
  private CompoundInterestCalculator calculator;

  /**
   * Creates the value before to test.
   */
  @Before
  public void createValueBeforeToTest() {
    // Crear una calculadora
    this.calculator = new CompoundInterestCalculatorImp();
    // Crear un objeto de inversion inicial
    this.initialInvestment = new InitialInvestmentDto();

    // Setear los valores de la inversion inicial
    this.initialInvestment.setInitialInvestment(5000.00);
    this.initialInvestment.setYearlyInput(3000.00);
    this.initialInvestment.setYearlyInputIncrement(1);
    this.initialInvestment.setInvestmentYears(5);
    this.initialInvestment.setInvestmentYield(21f);

    // Crea el controlador con su dependencia de calculadora (constructor)
    this.controller = new ApplicationController(this.calculator);
  }

  @Test
  public void shouldGenerateTableYield() {
    List<InvestmentYieldDto> tableYield = controller.createTableYield("application/json",
        initialInvestment);
    assertEquals(5, tableYield.size());

    /*InvestmentYieldDto firstYear = tableYield.get(0);
    assertEquals(Double.valueOf(5000.00), firstYear.getInitialInvestment());
    assertEquals(Double.valueOf(3000.00), firstYear.getYearlyInput());
    assertEquals(Double.valueOf(1680.00), firstYear.getInvestmentYield());
    assertEquals(Double.valueOf(9680.00), firstYear.getFinalBalance());*/
  }
}
