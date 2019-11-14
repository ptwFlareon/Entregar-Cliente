/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.contracts;

import br.com.rff.controller.TelaPesquisaController;
import br.com.rff.dao.DaoInterface;
import javafx.scene.Parent;

/**
 *
 * @author lukas
 */
public interface CrudInterface {

    //configura tela de crud
        public void configurar(DaoInterface dao, TelaPesquisaController pesquisaController, FormControllerInterface formController,String titulo);
    
    //retorna o layout do crud
    public Parent getLayout();
}
