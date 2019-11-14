/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.controller;

import br.com.rff.contracts.CrudInterface;
import br.com.rff.contracts.FormControllerInterface;
import br.com.rff.contracts.LookupControllerInterface;
import br.com.rff.dao.DaoInterface;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author lukas
 */
public class CrudViewController implements Initializable, CrudInterface {

    @FXML
    private AnchorPane apCrud;
    @FXML
    private Label lblTitulo;
    @FXML
    private Insets x1;
    @FXML
    private Button btNovo;
    @FXML
    private Button btEditar;
    @FXML
    private Button btSalvar;
    @FXML
    private Button btCancelar;
    @FXML
    private Button btRemover;
    @FXML
    private StackPane spContainer;

    private DaoInterface dao;

    private TelaPesquisaController pesquisaController;

    private FormControllerInterface formController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void novo(ActionEvent event) {
        this.spContainer.getChildren().clear();
        this.spContainer.getChildren().add(formController.getLayout());
        this.formController.inicializar();
        this.btNovo.setDisable(true);
        this.btEditar.setDisable(true);
        this.btSalvar.setDisable(!true);
        this.btCancelar.setDisable(false);
        this.btRemover.setDisable(true);
    }

    @FXML
    private void editar(ActionEvent event) {
        Object item = pesquisaController.getTblDados().getSelectionModel().getSelectedItem();
        novo(event);
        formController.setModel(item);

    }

    @FXML
    private void salvar(ActionEvent event) {
        dao.salvar(formController.getModel());
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setContentText("Salvo com sucesso!");
        msg.showAndWait();
        inicializar();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        inicializar();
    }

    @FXML
    private void remover(ActionEvent event) {
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION, "Deseja remover?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resposta = msg.showAndWait();
        if (resposta.get().equals(ButtonType.YES)) {
            Object item = pesquisaController.getTblDados().getSelectionModel().getSelectedItem();
            dao.remover(item);
            Alert rem = new Alert(Alert.AlertType.INFORMATION);
            rem.setContentText("Removido com sucesso!");
            rem.showAndWait();
            inicializar();
        }
    }

    @Override
    public void configurar(DaoInterface dao, TelaPesquisaController pesquisaController, FormControllerInterface formController, String titulo) {
        this.lblTitulo.setText(titulo);
        this.dao = dao;
        this.pesquisaController = pesquisaController;
        this.formController = formController;

        if (formController instanceof LookupControllerInterface) {
            ((LookupControllerInterface) formController).hasActiveLookup().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    btCancelar.setDisable(true);
                    btSalvar.setDisable(true);
                } else {
                    btCancelar.setDisable(!true);
                    btSalvar.setDisable(!true);
                }
            });
        }
        pesquisaController.getTblDados().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //selected
                btEditar.setDisable(false);
                btRemover.setDisable(false);
            } else {
                //not-selected
                btEditar.setDisable(true);
                btRemover.setDisable(!false);
            }
        });
        this.inicializar();
    }

    @Override
    public Parent getLayout() {
        return this.apCrud;
    }

    private void inicializar() {
        this.spContainer.getChildren().clear();
        this.spContainer.getChildren().add(pesquisaController.getLayout());

        btNovo.setDisable(!true);
        btEditar.setDisable(true);
        btSalvar.setDisable(true);
        btCancelar.setDisable(true);
    }

}
