package com.furj.danmu;

import com.furj.danmu.utils.ExcelReader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DanmakuApp extends Application {
    private int WINDOW_WIDTH;   // 窗口宽度
    private int WINDOW_HEIGHT;  // 窗口高度
    private double DANMAKU_SPEED;     // 字幕速度
    private int DANMAKU_FONT_SIZE; // 字幕字体大小
    private double DANMAKU_FONT_ALPHA; // 字幕字体透明度
    private Color DANMAKU_COLOR; // 弹幕颜色
    private int START_INDEX; // 起始行数
    private int BATCH_SIZE; // 数量
    private int WINDOW_X_COORDINATE;
    private int WINDOW_Y_COORDINATE;

    private Random random = new Random();

    public Stage stage;


    private SettingsController settingsController;

    private Pane root;    // 根Pane节点
    private List<Text> danmakus;    // 字幕列表
    public void setValues(){
        WINDOW_WIDTH = settingsController.windowWidthTextField.getText().isEmpty()? 1000 : Integer.parseInt(settingsController.windowWidthTextField.getText());
        WINDOW_HEIGHT = settingsController.windowHeightTextField.getText().isEmpty()? 600 : Integer.parseInt(settingsController.windowHeightTextField.getText());
        DANMAKU_SPEED = settingsController.danmakuSpeedTextField.getText().isEmpty()? 1 : Double.parseDouble(settingsController.danmakuSpeedTextField.getText());
        DANMAKU_FONT_SIZE = settingsController.danmakuFontSizeTextField.getText().isEmpty()? 24 : Integer.parseInt(settingsController.danmakuFontSizeTextField.getText());
        DANMAKU_FONT_ALPHA = settingsController.danmakuOpacityTextField.getText().isEmpty()? 0.8 : Double.parseDouble(settingsController.danmakuOpacityTextField.getText());
        DANMAKU_COLOR = Color.web(settingsController.danmakuColorTextField.getText().isEmpty()? "#FFFFFF" : settingsController.danmakuColorTextField.getText());
        START_INDEX = settingsController.startIndexTextField.getText().isEmpty()? 0 : Integer.parseInt(settingsController.startIndexTextField.getText());
        BATCH_SIZE = settingsController.batchSizeTextField.getText().isEmpty()? 10 : Integer.parseInt(settingsController.batchSizeTextField.getText());
        WINDOW_X_COORDINATE = settingsController.windowXCoordinateTextField.getText().isEmpty()? 0 : Integer.parseInt(settingsController.windowXCoordinateTextField.getText());
        WINDOW_Y_COORDINATE = settingsController.windowYCoordinateTextField.getText().isEmpty()? 0 : Integer.parseInt(settingsController.windowYCoordinateTextField.getText());
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InvalidFormatException, InterruptedException {
        // 创建主窗口根节点
        root = new Pane();
        // 创建 Danmaku 列表
        danmakus = new ArrayList<>();

        stage = primaryStage;

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/css/primary.css").toExternalForm());
        primaryStage.setTitle("Danmaku App");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);

        // 加载设置窗口并创建控制器对象
        Stage settingsStage = settingsStage();
        settingsController.initialize();
        settingsController.setStage(this);
        setValues();
        primaryStage.setX(WINDOW_X_COORDINATE);
        primaryStage.setY(WINDOW_Y_COORDINATE);
        createDanmaku();
        // 创建托盘图标
        createTrayIcon(primaryStage, settingsStage);

        // 创建动画定时器
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateDanmakus();
            }
        };
        timer.start();
    }

    private Stage settingsStage() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
        Pane settingsPane = loader.load();
        this.settingsController = loader.getController();

        Scene settingsScene = new Scene(settingsPane, 600, 200);
        settingsScene.setFill(Color.WHITE);
        stage.setScene(settingsScene);
        stage.setTitle("Settings");
        return stage;
    }

    public void createDanmaku() throws IOException, InvalidFormatException, InterruptedException {
        root.getChildren().removeAll(danmakus);
        danmakus.clear();
        String path = getClass().getResource("/考研词汇乱序版上册.xlsx").getPath();
        List<Text> texts = ExcelReader.readExcel(path, START_INDEX, BATCH_SIZE);
        for (Text text : texts) {
            // 创建一个新的Text对象，用于显示弹幕文本
            Text danmaku = new Text(text.getText());
            // 设置弹幕的字体为"Arial"，大小为成员变量的值
            danmaku.setFont(Font.font("Arial", DANMAKU_FONT_SIZE));
            // 设置弹幕文本的颜色为成员变量的值
            danmaku.setFill(DANMAKU_COLOR);
            danmaku.setOpacity(DANMAKU_FONT_ALPHA);
            // 设置弹幕的y坐标为一个随机位置，避免弹幕之间重叠
            danmaku.setTranslateY(getRandomYPosition(WINDOW_HEIGHT, DANMAKU_FONT_SIZE));
            // 将弹幕添加到danmaku列表中
            danmakus.add(danmaku);
            // 将弹幕添加到root节点中
            root.getChildren().add(danmaku);
            // 将弹幕置于最上层
            danmaku.toFront();
        }
    }


    private void updateDanmakus() {
        // 更新弹幕的位置和属性
        for (Text danmaku : danmakus) {
            // 更新弹幕的属性
            danmaku.setFont(Font.font("Arial", DANMAKU_FONT_SIZE));
            danmaku.setFill(DANMAKU_COLOR);
            danmaku.setOpacity(DANMAKU_FONT_ALPHA);

            // 更新弹幕的x坐标，每次更新距离原位置向右移动一个像素
            double x = danmaku.getTranslateX() - DANMAKU_SPEED;
            danmaku.setTranslateX(x);

            // 如果弹幕已经移出窗口左侧边界
            if (x + danmaku.getLayoutBounds().getWidth() < 0) {
                // 将弹幕放置到窗口右侧
                danmaku.setTranslateX(WINDOW_WIDTH);
            }
        }
    }

    private double getRandomYPosition(int height,int size) throws InterruptedException {
        double y = random.nextDouble() * (height - size);
        // 遍历已有的弹幕，检查是否与新弹幕重叠
        for (Text danmaku : danmakus) {
            if (Math.abs(danmaku.getTranslateY() - y) < size) {
                // 如果重叠，则重新生成一个随机的 y 坐标
                return y;
            }
        }
        return y;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createTrayIcon(Stage primaryStage,Stage settingsStage) {
        // 创建系统托盘图标
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            ClassLoader classLoader = DanmakuApp.class.getClassLoader();
            String imagePath = "icon.png";
            java.net.URL imageURL = classLoader.getResource(imagePath);
            Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
            // 创建托盘图标
            TrayIcon trayIcon = new TrayIcon(image);


            // 创建托盘菜单项
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    primaryStage.show();
                    primaryStage.toFront();
                });
            });

            MenuItem settingsItem = new MenuItem("Settings");
            settingsItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    settingsStage.show();
                    settingsStage.toFront();
                });
            });

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            // 创建托盘菜单
            PopupMenu trayMenu = new PopupMenu();
            trayMenu.add(showItem);
            trayMenu.add(settingsItem);
            trayMenu.addSeparator();
            trayMenu.add(exitItem);
            trayIcon.setPopupMenu(trayMenu);
            trayIcon.setImageAutoSize(true);


            // 将托盘图标添加到系统托盘
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
