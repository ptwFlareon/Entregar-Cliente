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
import br.com.rff.model.Estado;
import java.math.BigInteger;
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
public class EstadoDao extends AbstractDao implements TableModelInterface {

    public EstadoDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    protected String getTableName() {
        return "estado";
    }

    @Override
    public ArrayList<Object> getByCriterios(ICriteria c) {
        ArrayList<Object> ests = new ArrayList<>();
        //cria a instrução sql
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.SELECT);
        //Parametiza a instrução SQL
        sql.setCriterio(c);
        try {
            //Executa a sql
            ArrayList<HashMap<String, Object>> dados = this.executeSql(sql);
            if (!dados.isEmpty()) {

                for (HashMap<String, Object> row : dados) {
                    //cria um estado para cada linha que retornou do banco
                    Estado est = new Estado();
                    est.setId(((Long) row.get("id")));
                    est.setNome((String) row.get("nome"));
                    est.setUf((String) row.get("uf"));
                    ests.add(est);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ests;
    }

    @Override
    public void salvar(Object o) {
        Estado estado = (Estado) o;
        ISqlInstruction sql = this.newInstruction(ISqlInstruction.QueryType.INSERT);
        if (estado.getId() > 0) {
            sql = this.newInstruction(ISqlInstruction.QueryType.UPDATE);

        }

        if (sql instanceof ISqlUpdate) {
            //update
            ((ISqlUpdate) sql).addRowData("nome", estado.getNome());
            ((ISqlUpdate) sql).addRowData("uf", estado.getUf());
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(new IFilter("id", "=", Long.toString(estado.getId())));//String.valueOf()
            sql.setCriterio(criterio);

        } else if (sql instanceof ISQLInsert) {
            //Insert
            ((ISQLInsert) sql).getRowData().put("id", String.valueOf(estado.getId()) );
            ((ISQLInsert) sql).getRowData().put("nome", estado.getNome());
            ((ISQLInsert) sql).getRowData().put("uf", estado.getUf());
            
        }
        try {
            Object ret = this.executeSql(sql);
            if (sql instanceof ISQLInsert && ret instanceof Long) {
                estado.setId((Long) ret);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }

    @Override
    public void remover(Object o) {
        Estado estado = (Estado) o;
        if (estado.getId() > 0) {
            ISqlInstruction del = this.newInstruction(ISqlInstruction.QueryType.DELETE);
            ICriteria criterio = new ICriteria();
            criterio.addExpressions(new IFilter("id", "=", String.valueOf(estado.getId())));
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

        TableColumn<Object, Object> nome = new TableColumn<>("Nome estado");
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cols.add(nome);

        TableColumn<Object, Object> uf = new TableColumn<>("uf");
        uf.setCellValueFactory(new PropertyValueFactory<>("uf"));
        cols.add(uf);
        return cols;
    }

    @Override
    public ArrayList<Object> pesquisar(String param) {
        ICriteria criterio = new ICriteria();
        criterio.addExpressions(new IFilter("nome", "like", "%" + param + "%"));
        return this.getByCriterios(criterio);

    }

}
