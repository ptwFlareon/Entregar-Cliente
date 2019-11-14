/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rff.dao;

import br.com.rff.contracts.ICriteria;
import br.com.rff.contracts.IFilter;
import br.com.rff.contracts.ISQLInsert;
import br.com.rff.contracts.ISqlInstruction;
import br.com.rff.contracts.ISqlUpdate;
import br.com.rff.model.Cidade;
import br.com.rff.model.Cliente;
import br.com.rff.model.Estado;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author RENATO
 */
public class ClienteDao extends AbstractDao implements TableModelInterface {

    private CidadeDao daoCidade;

    public ClienteDao(Connection conn) {
        this.conn = conn;
        this.daoCidade = new CidadeDao(conn);
    }

    @Override
    protected String getTableName() {
        return "cliente";
    }

    @Override
    public ArrayList<Object> getByCriterios(ICriteria c) {
        ArrayList<Object> clits = new ArrayList<>();
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.SELECT);
        sql.setCriterio(c);
        try {
            ArrayList<HashMap<String, Object>> dados = this.executeSql(sql);
            if (!dados.isEmpty()) {

                for (HashMap<String, Object> row : dados) {
                    Cliente clit = new Cliente();
                    clit.setId((Long) row.get("id"));
                    clit.setNome((String) row.get("nome"));

                    //cid.setEstado(new Estado());
                    if (((Long) row.get("idCidade")).intValue() > 0) {
                        clit.setCidade(
                                ((ArrayList<Cidade>) daoCidade.getById(
                                        ((Long) row.get("idCidade")).longValue())).get(0)
                        );
                    }

                    clits.add(clit);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clits;
    }

    @Override
    public void salvar(Object o) {
        Cliente cliente = (Cliente) o;
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.INSERT);
        if (cliente.getId() > 0) {
            sql = this.newInstruction(ISqlInstruction.QueryType.UPDATE);
        }

        if (sql instanceof ISqlUpdate) {
            //updatecol
            ((ISqlUpdate) sql).addRowData("nome", cliente.getNome());
            ((ISqlUpdate) sql).addRowData("idCidade", Long.toString(cliente.getCidade().getId()));
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(new IFilter("id", "=",
                    Long.toString(cliente.getId()))
            );
            sql.setCriterio(criterio);

        } else if (sql instanceof ISQLInsert) {
            //insert
            ((ISQLInsert) sql).getRowData().put("id", String.valueOf(cliente.getId()));
            ((ISQLInsert) sql).getRowData().put("nome", cliente.getNome());
            ((ISQLInsert) sql).getRowData().put("idCidade", String.valueOf(cliente.getCidade().getId()));
        }
        try {
            Object ret = this.executeSql(sql);
            if (sql instanceof ISQLInsert && ret instanceof Long) {
                cliente.setId((Long) ret);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void remover(Object o) {
        Cliente cliente = (Cliente) o;
        if (cliente.getId() > 0) {
            ISqlInstruction del = this.newInstruction(ISqlInstruction.QueryType.DELETE);
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(
                    new IFilter("id", "=", String.valueOf(cliente.getId())));
            del.setCriterio(criterio);

            try {
                executeSql(del);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public Object getById(long id) {
        ICriteria criterio = new ICriteria();
        criterio.addExpressions(new IFilter("id", "=", String.valueOf(id)));
        return this.getByCriterios(criterio);
    }

    @Override
    public ArrayList<TableColumn<Object, Object>> getCols() {
        ArrayList<TableColumn<Object, Object>> cols = new ArrayList<>();

        TableColumn<Object, Object> nome = new TableColumn<>("Nome cliente");
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cols.add(nome);

        TableColumn<Object, Object> cidade = new TableColumn<>("Cidade");
        cidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        cols.add(cidade);

        return cols;
    }

    @Override
    public ArrayList<Object> pesquisar(String param) {
        ICriteria criterio = new ICriteria();
        criterio.addExpressions(new IFilter("nome", "like", "%" + param + "%"));
        return this.getByCriterios(criterio);
    }

}
