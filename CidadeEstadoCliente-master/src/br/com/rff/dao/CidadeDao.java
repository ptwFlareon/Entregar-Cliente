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
public class CidadeDao extends AbstractDao implements TableModelInterface {

    private EstadoDao daoEstado;

    public CidadeDao(Connection conn) {
        this.conn = conn;
        this.daoEstado = new EstadoDao(conn);
    }

    @Override
    protected String getTableName() {
        return "cidade";
    }

    @Override
    public ArrayList<Object> getByCriterios(ICriteria c) {
        ArrayList<Object> cids = new ArrayList<>();
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.SELECT);
        sql.setCriterio(c);
        try {
            ArrayList<HashMap<String, Object>> dados = this.executeSql(sql);
            if (!dados.isEmpty()) {

                for (HashMap<String, Object> row : dados) {
                    Cidade cid = new Cidade();
                    cid.setId((Long) row.get("id"));
                    cid.setNome((String) row.get("nome"));

                    //cid.setEstado(new Estado());
                    if (((Long) row.get("idEstado")).intValue() > 0) {
                        cid.setEstado(
                                ((ArrayList<Estado>) daoEstado.getById(
                                        ((Long) row.get("idEstado")).longValue())).get(0)
                        );
                    }

                    cids.add(cid);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cids;
    }

    @Override
    public void salvar(Object o) {
        Cidade cidade = (Cidade) o;
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.INSERT);
        if (cidade.getId() > 0) {
            sql = this.newInstruction(ISqlInstruction.QueryType.UPDATE);
        }

        if (sql instanceof ISqlUpdate) {
            //updatecol
            ((ISqlUpdate) sql).addRowData("nome", cidade.getNome());
            ((ISqlUpdate) sql).addRowData("idEstado", Long.toString(cidade.getEstado().getId()));
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(new IFilter("id", "=",
                    Long.toString(cidade.getId()))
            );
            sql.setCriterio(criterio);

        } else if (sql instanceof ISQLInsert) {
            //insert
            ((ISQLInsert) sql).getRowData().put("id", String.valueOf(cidade.getId()));
            ((ISQLInsert) sql).getRowData().put("nome", cidade.getNome());
            ((ISQLInsert) sql).getRowData().put("idEstado", String.valueOf(cidade.getEstado().getId()));
        }
        try {
            Object ret = this.executeSql(sql);
            if (sql instanceof ISQLInsert && ret instanceof Long) {
                cidade.setId((Long) ret);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void remover(Object o) {
        Cidade cidade = (Cidade) o;
        if (cidade.getId() > 0) {
            ISqlInstruction del = this.newInstruction(ISqlInstruction.QueryType.DELETE);
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(
                    new IFilter("id", "=", String.valueOf(cidade.getId())));
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

        TableColumn<Object, Object> nome = new TableColumn<>("Nome Cidade");
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cols.add(nome);

        TableColumn<Object, Object> estadoUF = new TableColumn<>("Estado");
        estadoUF.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cols.add(estadoUF);

        return cols;
    }

    @Override
    public ArrayList<Object> pesquisar(String param) {
        ICriteria criterio = new ICriteria();
        criterio.addExpressions(new IFilter("nome", "like", "%" + param + "%"));
        return this.getByCriterios(criterio);
    }

}
