/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.controller;

import br.com.rff.contracts.FormControllerInterface;
import br.com.rff.model.Estado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author lukas
 */
public class FormEstadoController implements Initializable, FormControllerInterface {
    @FXML
    private AnchorPane apEstado;
    @FXML
    private Font x1;
    @FXML
    private Insets x2;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtSigla;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializar();
    }    

        
    private Estado model;
    
    @Override
    public Parent getLayout() {
        return this.apEstado;
    }

    @Override
    public void setModel(Object model) {
        this.model = (Estado) model;
        txtNome.setText(this.model.getNome());
        txtSigla.setText(this.model.getUf());
    }

    @Override
    public Object getModel() {
        this.model.setNome(txtNome.getText());
        this.model.setUf(txtSigla.getText());
        return this.model;
    }
    
    public void inicializar() {
        this.setModel(new Estado());
    }
    
}
