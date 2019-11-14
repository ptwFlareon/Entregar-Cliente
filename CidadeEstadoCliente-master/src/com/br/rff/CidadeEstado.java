/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rff;

import br.com.rff.contracts.CrudInterface;
import br.com.rff.contracts.FormControllerInterface;
import br.com.rff.contracts.LookupControllerInterface;
import br.com.rff.controller.LookupController;
import br.com.rff.controller.TelaPesquisaController;
import br.com.rff.dao.CidadeDao;
import br.com.rff.dao.ClienteDao;
import br.com.rff.dao.DaoInterface;
import br.com.rff.dao.EstadoDao;
import br.com.rff.dao.TableModelInterface;
import br.com.rff.services.Conexao;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author RENATO
 */
public class CidadeEstado extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loadFormEstado = new FXMLLoader(getClass().getResource("/br/com/rff/view/FormEstado.fxml"));
        Parent formEstado = loadFormEstado.load();
        FormControllerInterface ce = loadFormEstado.getController();

        FXMLLoader loadFormCidade = new FXMLLoader(getClass().getResource("/br/com/rff/view/FormCidade.fxml"));
        Parent formCidade = loadFormCidade.load();
        FormControllerInterface cc = loadFormCidade.getController();

        FXMLLoader loadFormCliente = new FXMLLoader(getClass().getResource("/br/com/rff/view/FormCliente.fxml"));
        Parent formCliente = loadFormCliente.load();
        FormControllerInterface ccli = loadFormCliente.getController();

        FXMLLoader loadLookup = new FXMLLoader(getClass().getResource("/br/com/rff/view/Lookup.fxml"));
        Parent formLookup = loadLookup.load();
        FormControllerInterface cl = loadLookup.getController();

        FXMLLoader loadCrudView = new FXMLLoader(getClass().getResource("/br/com/rff/view/CrudView.fxml"));
        Parent crudView = loadCrudView.load();
        CrudInterface crudController = loadCrudView.getController();

        //Carrega o layout e o controller principal
        FXMLLoader loaderPrincipal = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loaderPrincipal.load();
        FXMLDocumentController pc = loaderPrincipal.getController();

        //Carrega o layout e o controller de pesquisa
        FXMLLoader loaderPesquisa = new FXMLLoader(getClass().getResource("/br/com/rff/view/TelaPesquisa.fxml"));
        Parent bp = loaderPesquisa.load();
        TelaPesquisaController tpc = loaderPesquisa.getController();

        pc.getBtnCidade().addEventHandler(ActionEvent.ACTION, (event) -> {

            ((LookupController) cl).configurar(new EstadoDao(Conexao.getInstance().getConn()), "Pesquisar estado");
            TableModelInterface tm = new CidadeDao(Conexao.getInstance().getConn());
            tpc.configure(tm);
            ((LookupControllerInterface) cc).setLookUp((LookupControllerInterface) cl);

            crudController.configurar((DaoInterface) tm, tpc, cc, "Cidades");
            pc.getContainer().getChildren().clear();
            pc.getContainer().getChildren().add(crudView);
            stage.setTitle("PESQUISA DE CIDADE");

        });

        pc.getBtnCliente().addEventHandler(ActionEvent.ACTION, (event) -> {

            ((LookupController) cl).configurar(new CidadeDao(Conexao.getInstance().getConn()), "Pesquisar cidade");
            TableModelInterface tm = new ClienteDao(Conexao.getInstance().getConn());
            tpc.configure(tm);
            ((LookupControllerInterface) ccli).setLookUp((LookupControllerInterface) cl);

            crudController.configurar((DaoInterface) tm, tpc, ccli, "Clientes");
            pc.getContainer().getChildren().clear();
            pc.getContainer().getChildren().add(crudView);
            stage.setTitle("PESQUISA DE CLIENTE");

        });

        pc.getBtnEstado().addEventHandler(ActionEvent.ACTION, (event) -> {
            TableModelInterface tm = new EstadoDao(Conexao.getInstance().getConn());
            tpc.configure(tm);
            crudController.configurar((DaoInterface) tm, tpc, ce, "Estados");
            pc.getContainer().getChildren().clear();
            pc.getContainer().getChildren().add(crudView);
            stage.setTitle("PESQUISA DE ESTADO");
        });

        Scene scene = new Scene(root);
        //Scene scene = new Scene(formEstado);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

        // Estado e = new Estado();
        //e.setId(1);
        // e.setNome("PARANÁ");
        // e.setUf("PR");
        // Cidade c = new Cidade();
        // c.setId(1);
        // c.setNome("Palotina");
        // c.setEstado(e);
        // System.out.println(c.getEstado().getNome());
        //  System.out.println(c.getEstado().getNome().replace("A", "I"));
    }

}
