/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.controller;

import br.com.rff.contracts.LookupControllerInterface;
import br.com.rff.model.Cidade;
import br.com.rff.model.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author lukas
 */
public class FormClienteController implements Initializable, LookupControllerInterface {

    @FXML
    private AnchorPane apCliente;
    @FXML
    private Font x1;
    @FXML
    private TextField txtNome;
    @FXML
    private Insets x2;
    @FXML
    private TextField txtCidade;
    @FXML
    private Button btLookup;
    private Cliente cliente;
    private LookupController lkp;
    private BooleanProperty hasActiveLookup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.hasActiveLookup = new SimpleBooleanProperty(false);
    }

    @FXML
    private void buscarCidade(ActionEvent event) {
        StackPane parent = (StackPane) apCliente.getParent();
        parent.getChildren().add(lkp.getLayout());
        lkp.inicializar();
        this.hasActiveLookup.setValue(Boolean.TRUE);

        Task<Object> lookup = new Task() {
            @Override
            protected Object call() throws Exception {
                while (!lkp.isCloseRequested()) {
                    Thread.sleep(250);
                }
                return lkp.getModel();
            }
        };

        lookup.setOnSucceeded((evt) -> {
            if (lookup.getValue() != null) {
                cliente.setCidade((Cidade) lookup.getValue());
                setModel(this.getModel());
            }
            this.hasActiveLookup.setValue(Boolean.FALSE);
            parent.getChildren().remove(lkp.getLayout());
        });

        new Thread(lookup).start();
    }

    @Override
    public void setLookUp(LookupControllerInterface lkp) {
        this.lkp = (LookupController) lkp;
    }

    @Override
    public BooleanProperty hasActiveLookup() {
        return this.hasActiveLookup;
    }

    @Override
    public Parent getLayout() {
        return this.apCliente;
    }

    @Override
    public void setModel(Object model) {
        this.cliente = (Cliente) model;
        txtNome.setText(this.cliente.getNome());
        if (this.cliente.getCidade() != null) {
            txtCidade.setText(this.cliente.getCidade().getNome());
        } else {
            txtCidade.setText("");
        }
    }

    @Override
    public Object getModel() {
        this.cliente.setNome(txtNome.getText());

        return this.cliente;
    }

    @Override
    public void inicializar() {
        this.setModel(new Cliente());
    }

}
