package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class HelloController {
    public GridPane pnLogin;
    public AnchorPane pnMain;
    public VBox pnHome;
    public Button btnSingInUser;
    public Button btnDeteUser;
    public Button btnRegisterUser;
    public TextField tfUsName;
    public TextField tfPass;
    public TextField tfEmo;
    public Button btnRegisterUser1;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {try{
            Connection c = MYSQLConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM emotions WHERE Username=? AND Password=?");
            ps.setString(1, tfUsName.getText());
            ps.setString(2, tfPass.getText());
            int rowDel = ps.executeUpdate();
            System.out.println("Rows Deleted: " + rowDel);
        }catch(SQLException e){

        }
    }
    @FXML
    protected void onSigninClick() throws IOException {
        try(Connection C = MYSQLConnection.getConnection()){
        PreparedStatement ps = C.prepareStatement("SELECT emotion FROM emotions WHERE Username=? AND Password=?");
        ps.setString(1, tfUsName.getText());
        ps.setString(2, tfPass.getText());
        ResultSet res = ps.executeQuery();

        while(res.next()){
//            int id = res.getInt("id");
//            String name = res.getString("name");
            String Emo = res.getString("Emotion");
            System.out.println(Emo);
            tfEmo.setText(Emo);
            btnRegisterUser1.setDisable(false);
        }
    }catch (SQLException e){

    }
//        Parent homeview = FXMLLoader.load(HelloApplication.class
//                .getResource("homepage.fxml"));
//        AnchorPane p = (AnchorPane) pnLogin.getParent();
//        p.getChildren().remove(pnLogin);
//        p.getChildren().add(homeview);
    }

    @FXML
    protected void CreateUser(){
        try {
            Connection C = MYSQLConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("INSERT INTO emotions (Username,Password,Emotion) VALUES (?,?,\"\")");
            ps.setString(1, tfUsName.getText());
            ps.setString(2, tfPass.getText());
            System.out.println(ps.toString());
            ps.executeUpdate();
            System.out.println("Done");
        }catch (SQLException e){

        }
    }

    @FXML
    protected void SetEmo(){
        try {
            Connection C = MYSQLConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("UPDATE emotions SET emotion=? WHERE Username=? AND Password=?");
            ps.setString(1, tfEmo.getText());
            ps.setString(2, tfUsName.getText());
            ps.setString(3, tfPass.getText());
            int ret = ps.executeUpdate();
            System.out.println("res : " + ret);
        }catch(SQLException e){

        }
    }

}