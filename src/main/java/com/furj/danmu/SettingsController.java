package com.furj.danmu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class SettingsController {


    @FXML
    public TextField startIndexTextField;// 起始行数

    @FXML
    public TextField batchSizeTextField;// 数量

    @FXML
    public Label selectedFilePathLabel; // 选中的文件路径

    @FXML
    private VBox root;

    @FXML
    public TextField windowWidthTextField;// 窗口宽度

    @FXML
    public TextField windowHeightTextField;// 窗口高度

    @FXML
    public TextField danmakuSpeedTextField;// 弹幕速度

    @FXML
    public TextField danmakuColorTextField;// 弹幕颜色

    @FXML
    public TextField danmakuFontSizeTextField;// 弹幕字体大小

    @FXML
    public TextField danmakuOpacityTextField;// 弹幕透明度

    @FXML
    public TextField windowXCoordinateTextField;// 窗口 X 坐标

    @FXML
    public TextField windowYCoordinateTextField;// 窗口 Y 坐标

    @FXML
    public TextField wordIntervalTextField; // 文字间隔

    @FXML
    private Button applyButton;

    private Properties properties;

    private DanmakuApp stage;

    public void setStage(DanmakuApp stage) {
        this.stage = stage;
    }

    @FXML
    public void applySettings() throws IOException, InvalidFormatException {
        stage.setValues();
        stage.stage.setX(Integer.parseInt(windowXCoordinateTextField.getText()));
        stage.stage.setY(Integer.parseInt(windowYCoordinateTextField.getText()));
        stage.stage.setWidth(Integer.parseInt(windowWidthTextField.getText()));
        stage.stage.setHeight(Integer.parseInt(windowHeightTextField.getText()));
        //弹幕文字列表
        properties.setProperty("start_index", startIndexTextField.getText() != null && !startIndexTextField.getText().isEmpty() ? startIndexTextField.getText() : "0");
        properties.setProperty("batch_size", batchSizeTextField.getText() != null && !batchSizeTextField.getText().isEmpty() ? batchSizeTextField.getText() : "10");
        properties.setProperty("word_interval", wordIntervalTextField.getText()!= null &&!wordIntervalTextField.getText().isEmpty()? wordIntervalTextField.getText() : "5");
        properties.setProperty("selected_file_path", (selectedFilePathLabel.getText() == null || Objects.equals(selectedFilePathLabel.getText(), "")) ? "" : selectedFilePathLabel.getText());
        stage.createDanmaku();
        //窗口设置
        properties.setProperty("window_width", windowWidthTextField.getText() != null && !windowWidthTextField.getText().isEmpty() ? windowWidthTextField.getText() : "1000");
        properties.setProperty("window_height", windowHeightTextField.getText() != null && !windowHeightTextField.getText().isEmpty() ? windowHeightTextField.getText() : "600");
        properties.setProperty("window_x", windowXCoordinateTextField.getText() != null && !windowXCoordinateTextField.getText().isEmpty() ? windowXCoordinateTextField.getText() : "0");
        properties.setProperty("window_y", windowYCoordinateTextField.getText() != null && !windowYCoordinateTextField.getText().isEmpty() ? windowYCoordinateTextField.getText() : "0");
        //弹幕文字设置
        properties.setProperty("danmaku_speed", danmakuSpeedTextField.getText() != null && !danmakuSpeedTextField.getText().isEmpty() ? danmakuSpeedTextField.getText() : "1");
        properties.setProperty("danmaku_color", danmakuColorTextField.getText() != null && !danmakuColorTextField.getText().isEmpty() ? danmakuColorTextField.getText() : "#FFFFFF");
        properties.setProperty("danmaku_font_size", danmakuFontSizeTextField.getText() != null && !danmakuFontSizeTextField.getText().isEmpty() ? danmakuFontSizeTextField.getText() : "16");
        properties.setProperty("danmaku_opacity", danmakuOpacityTextField.getText() != null && !danmakuOpacityTextField.getText().isEmpty() ? danmakuOpacityTextField.getText() : "0.8");
        try {
            FileOutputStream outputStream = new FileOutputStream("config.properties");
            properties.store(outputStream, null);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // 加载配置文件
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            // 如果配置文件不存在，则创建一个新的 Properties 对象并设置默认值
            properties.setProperty("start_index", "0");
            properties.setProperty("batch_size", "10");
            properties.setProperty("window_width", "1000");
            properties.setProperty("window_height", "600");
            properties.setProperty("danmaku_speed", "1");
            properties.setProperty("danmaku_color", "#FFFFFF");
            properties.setProperty("danmaku_font_size", "16");
            properties.setProperty("danmaku_opacity", "0.8");
            properties.setProperty("window_x", "0");
            properties.setProperty("window_y", "0");
            properties.setProperty("word_interval", "5");
            properties.setProperty("selected_file_path", "");

            // 将新的 Properties 对象写入配置文件中
            try {
                FileOutputStream outputStream = new FileOutputStream("config.properties");
                properties.store(outputStream, null);
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // 从配置文件中获取属性值，并将其设置到对应的文本框中
        startIndexTextField.setText(properties.getProperty("start_index"));
        batchSizeTextField.setText(properties.getProperty("batch_size"));
        windowWidthTextField.setText(properties.getProperty("window_width"));
        windowHeightTextField.setText(properties.getProperty("window_height"));
        danmakuSpeedTextField.setText(properties.getProperty("danmaku_speed"));
        danmakuColorTextField.setText(properties.getProperty("danmaku_color"));
        danmakuFontSizeTextField.setText(properties.getProperty("danmaku_font_size"));
        danmakuOpacityTextField.setText(properties.getProperty("danmaku_opacity"));
        windowXCoordinateTextField.setText(properties.getProperty("window_x"));
        windowYCoordinateTextField.setText(properties.getProperty("window_y"));
        wordIntervalTextField.setText(properties.getProperty("word_interval"));
        selectedFilePathLabel.setText(properties.getProperty("selected_file_path"));
    }
    @FXML
    public void selectFile() throws IOException, InvalidFormatException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))); // start in the current directory
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")); // restrict to .xlsx files
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            // Update the text field with the selected file path
            // For example:
            System.out.println(selectedFile.getAbsolutePath());
            selectedFilePathLabel.setText(selectedFile.getAbsolutePath());
            startIndexTextField.setText("0");
            applySettings();
        }
    }




}
