/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.contracts;

import javafx.scene.Parent;

/**
 *
 * @author lukas
 */
public interface FormControllerInterface {

    public Parent getLayout();

    public void setModel(Object model);

    public Object getModel();
    
    public void inicializar();
}
