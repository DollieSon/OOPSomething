package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {
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
    public Label lblMoney;
    public Button btnRemoveMoney;
    public Button btnAddMoney;
    @FXML
    private Label welcomeText;

    private String UserID;

    @FXML
    protected void onHelloButtonClick() {try{
            Connection c = MYSQLConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM emotions WHERE Username=? AND Password=?; ");
            ps.setString(1, tfUsName.getText());
            ps.setString(2, tfPass.getText());
            PreparedStatement ps2 =c.prepareStatement("DELETE FROM bankvault WHERE UID=?;");
            ps2.setString(1,UserID);
            ps2.executeUpdate();
            int rowDel = ps.executeUpdate();
            System.out.println("Rows Deleted: " + rowDel);
            btnRegisterUser1.setDisable(true);
            btnAddMoney.setDisable(true);
            btnRemoveMoney.setDisable(true);
        }catch(SQLException e){

        }
    }
    @FXML
    protected void onSigninClick() throws IOException {
        try(Connection C = MYSQLConnection.getConnection()){
        PreparedStatement ps = C.prepareStatement("SELECT emotion,UID FROM emotions WHERE Username=? AND Password=?");
        ps.setString(1, tfUsName.getText());
        ps.setString(2, tfPass.getText());
        ResultSet res = ps.executeQuery();

        while(res.next()){
//            int id = res.getInt("id");
            UserID = res.getString("UID");
            String Emo = res.getString("Emotion");
            System.out.println(Emo);
            tfEmo.setText(Emo);
        }
        btnRegisterUser1.setDisable(false);
        btnAddMoney.setDisable(false);
        btnRemoveMoney.setDisable(false);
        PreparedStatement ps2 = C.prepareStatement("SELECT MoneyInside FROM bankvault WHERE UID=?");
        ps2.setString(1,UserID);
        res = ps2.executeQuery();
        while(res.next()){
            String money = res.getString("MoneyInside");
            lblMoney.setText(money);

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
        try (Connection C = MYSQLConnection.getConnection();){
            PreparedStatement ps = C.prepareStatement("INSERT INTO emotions (Username,Password,Emotion) VALUES (?,?,\"\");");
            ps.setString(1, tfUsName.getText());
            ps.setString(2, tfPass.getText());
            ps.executeUpdate();
            PreparedStatement ps2 = C.prepareStatement("SELECT UID FROM emotions WHERE Username=? AND Password=?");
            ps2.setString(1, tfUsName.getText());
            ps2.setString(2, tfPass.getText());
            ResultSet res = ps2.executeQuery();
            while(res.next()){
                UserID = res.getString("UID");
                System.out.printf(UserID);
            }

            PreparedStatement ps3 = C.prepareStatement("INSERT INTO bankvault (UID,MoneyInside) VALUES(?,?)");
            ps3.setString(1,UserID);
            ps3.setInt(2,1000);
            System.out.println(ps.toString());
            ps3.executeUpdate();
            System.out.println("Done");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void SetEmo(){
        try (Connection C = MYSQLConnection.getConnection();){
            PreparedStatement ps = C.prepareStatement("UPDATE emotions SET emotion=? WHERE Username=? AND Password=?");
            ps.setString(1, tfEmo.getText());
            ps.setString(2, tfUsName.getText());
            ps.setString(3, tfPass.getText());
            int ret = ps.executeUpdate();
            System.out.println("res : " + ret);
        }catch(SQLException e){

        }
    }
@FXML
    protected void AddMoney(){
        ChangeMoney(10,UserID);
    }
    @FXML
    protected void RemoveMoney(){
        ChangeMoney(-10,UserID);
    }

    protected void ChangeMoney(int Amnt,String Id){
        try(Connection C = MYSQLConnection.getConnection()) {
            int Money =Integer.parseInt( lblMoney.getText());
            Money+=Amnt;
            PreparedStatement ps = C.prepareStatement("UPDATE bankvault SET MoneyInside=? WHERE UID=?");
            ps.setString(1,Integer.toString(Money));
            ps.setString(2,Id);
            int ret = ps.executeUpdate();
            System.out.println("res : " + ret);
            PreparedStatement ps2 = C.prepareStatement("SELECT MoneyInside FROM bankvault WHERE UID=?");
            ps2.setString(1,UserID);
            ResultSet res = ps2.executeQuery();
            while(res.next()){
                String money = res.getString("MoneyInside");
                lblMoney.setText(money);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try(Connection C = MYSQLConnection.getConnection()){
        String querry = (" CREATE TABLE IF NOT EXISTS `emotions` (`UID` INT PRIMARY KEY NOT NULL AUTO_INCREMENT ," +
                        "`Username` VARCHAR(255) NOT NULL , "+
                        "`Password` VARCHAR(255) NOT NULL , " +
                        "`emotion` VARCHAR(255) NOT NULL"+
                   ");");
        String table2 = "CREATE TABLE IF NOT EXISTS `dbseratodollisonf1`.`bankvault` (`bankID` INT PRIMARY KEY NOT NULL AUTO_INCREMENT , `UID` INT NOT NULL , `MoneyInside` INT NOT NULL);";
         Statement st = C.createStatement();
         st.execute(querry);
         st.execute(table2);
        System.out.println("innit Table Created Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}