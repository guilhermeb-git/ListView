package com.example.listview;

import Dao.TarefaDao;
import Models.Tarefa;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TarefaController {

    @FXML private TableView<Tarefa> tableView;
    @FXML private TableColumn<Tarefa, String> colTitulo, colDescricao, colStatus;
    @FXML private TableColumn<Tarefa, LocalDate> colDataEntrega;
    @FXML private Label lblRelogio;

    private final TarefaDao tarefaDAO = new TarefaDao();
    private final ObservableList<Tarefa> tarefas = FXCollections.observableArrayList();

    public void initialize() {
        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colDescricao.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescricao()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        colDataEntrega.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDataEntrega()));

        atualizarLista();
        iniciarRelogio();
    }

    @FXML
    private void atualizarLista() {
        try {
            tarefas.setAll(tarefaDAO.listar());
            tableView.setItems(tarefas);
        } catch (SQLException e) {
            mostrarAlertaErro("Erro ao carregar tarefas: " + e.getMessage());
        }
    }

    @FXML
    private void adicionarTarefa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/listview/formulario_tarefa.fxml"));
            Parent root = loader.load();

            FormularioTarefaController controller = loader.getController();
            controller.setTarefa(null); // Nova tarefa

            Stage stage = new Stage();
            stage.setTitle("Adicionar Nova Tarefa");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Espera o formulário fechar

            atualizarLista(); // Atualiza a tabela depois de adicionar a tarefa

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir formulário: " + e.getMessage());
        }
    }

    @FXML
    private void editarTarefa() {
        Tarefa selecionada = tableView.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/listview/formulario_tarefa.fxml"));
                Parent root = loader.load();

                FormularioTarefaController controller = loader.getController();
                controller.setTarefa(selecionada); // Passa a tarefa selecionada para editar

                Stage stage = new Stage();
                stage.setTitle("Editar Tarefa");
                stage.setScene(new Scene(root));
                stage.showAndWait(); // Espera o formulário fechar

                atualizarLista(); // Atualiza depois da edição

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlertaErro("Erro ao abrir formulário: " + e.getMessage());
            }
        } else {
            mostrarAlertaErro("Nenhuma tarefa selecionada para editar!");
        }
    }

    @FXML
    private void excluirTarefa() {
        Tarefa selecionada = tableView.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                tarefaDAO.deletar(selecionada.getId());
                atualizarLista();
            } catch (SQLException e) {
                mostrarAlertaErro("Erro ao excluir tarefa: " + e.getMessage());
            }
        } else {
            mostrarAlertaErro("Nenhuma tarefa selecionada!");
        }
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void iniciarRelogio() {
        Thread thread = new Thread(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            while (true) {
                Platform.runLater(() -> lblRelogio.setText(LocalDateTime.now().format(formatter)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
