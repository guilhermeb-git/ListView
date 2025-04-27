package com.example.listview;

import Dao.TarefaDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Tarefa;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioTarefaController {

    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescricao;
    @FXML private DatePicker dpDataEntrega;
    @FXML private ComboBox<String> cbStatus;

    private Tarefa tarefa; // tarefa que estamos editando ou criando
    private final TarefaDao tarefaDAO = new TarefaDao();

    public void initialize() {
        cbStatus.getItems().addAll("A Fazer", "Em Andamento", "Concluído");
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
        if (tarefa != null) {
            txtTitulo.setText(tarefa.getTitulo());
            txtDescricao.setText(tarefa.getDescricao());
            dpDataEntrega.setValue(tarefa.getDataEntrega());
            cbStatus.setValue(tarefa.getStatus());
        }
    }

    @FXML
    private void salvarTarefa() {
        if (txtTitulo.getText().isEmpty() || dpDataEntrega.getValue() == null || cbStatus.getValue() == null) {
            mostrarAlertaErro("Preencha todos os campos obrigatórios!");
            return;
        }

        if (tarefa == null) {
            tarefa = new Tarefa();
        }

        tarefa.setTitulo(txtTitulo.getText());
        tarefa.setDescricao(txtDescricao.getText());
        tarefa.setDataEntrega(dpDataEntrega.getValue());
        tarefa.setStatus(cbStatus.getValue());

        try {
            if (tarefa.getId() == 0) {
                tarefaDAO.adicionar(tarefa);
            } else {
                tarefaDAO.atualizar(tarefa);
            }
            fechar();
        } catch (SQLException e) {
            mostrarAlertaErro("Erro ao salvar tarefa: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
