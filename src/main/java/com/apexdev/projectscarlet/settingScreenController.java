package com.apexdev.projectscarlet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class settingScreenController implements Initializable {

    private String selectedUsername;

    @FXML
    Text name;

    private Parent root;


    @FXML
    private TextField search;



    @FXML
    private TableView<userDB> availableUsers;

    @FXML
    private TableColumn<userDB, String> firstNameColumn;;


    private ArrayList<String> usernames = new ArrayList<String>();



    @FXML
    void add(MouseEvent event) throws IOException {

        // What to do when pressing add button

        boolean isAlreadyAdded = false;
        System.out.println(isAlreadyAdded);

        for (int j = 0; j < usernames.size(); j++) {
            if (search.getText().equals(usernames.get(j))) {
                //If already added, notify user
                System.out.println("Can't Add another User");
                isAlreadyAdded = true;
                System.out.println(isAlreadyAdded);
            }
        }

        if (!isAlreadyAdded) {
            //If not added then add it to both arraylist and database
            availableUsers.setItems(populateTableView(search.getText()));
            usernames.add(search.getText());
            savingUsernames();
        }


    }



    @FXML
    void back(MouseEvent event) throws IOException {

        //Go to home screen if clicked
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homeScreen.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 600, 400);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(true);
        stage.setFullScreen(false);




    }

    @FXML
    void remove(MouseEvent event) throws IOException {

        //Remove the person in the text box if pressed button "remove"
        boolean isRemoved = false;
        for (int j = 0; j < usernames.size(); j++) {
            System.out.println(usernames.get(j));
            if (search.getText().equals(usernames.get(j))) {
                availableUsers.getItems().remove(j);
                usernames.remove(j);
                isRemoved= true;
                savingUsernames();
            }
        }

        if (isRemoved) {
            System.out.println("Sucessfully Removed");
        } else {
            System.out.println("Failed to remove. Check Name. Make sure you written the name in the Text Box Below");
        }


    }

    @FXML
    void select(MouseEvent event) throws FileNotFoundException {
        //Change user and setup stuff so that's the same when restarted the app
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(search.getText())) {
                System.out.println("Changing User");
                name.setText(search.getText());
                selectedUsername = search.getText();

                PrintWriter pw = new PrintWriter("CurrentUser.txt");
                pw.println(search.getText());
                pw.close();


                break;
            }
        }

        if (!usernames.contains(search.getText())){
            System.out.println("The User does not exist \nMaybe add the User??");
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        //Setup the columns in Table View
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<userDB, String>("firstName"));


        try {
            //Simple file readers and scanners to create the new array and database upon startup
            FileReader usernameGetter = new FileReader("Users.txt");
            Scanner usernameScanner = new Scanner(usernameGetter);






            while (usernameScanner.hasNext()) {

                usernames.add(usernameScanner.nextLine());

            }


            for (int i = 0; i < usernames.size(); i++) {
                //Populate the tableview
                availableUsers.setItems(populateTableView(usernames.get(i)));
            }

            usernameGetter.close();
            usernameScanner.close();

            savingUsernames();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            //Check current user and setup that as main user upon startup
            FileReader fr = new FileReader("CurrentUser.txt");
            Scanner fileScanner = new Scanner(fr);
            selectedUsername = fileScanner.nextLine();
            name.setText(selectedUsername);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private ObservableList<userDB> people = FXCollections.observableArrayList();

    private ObservableList<userDB> populateTableView(String username) throws IOException {
        //Well populate the table
            people.add(new userDB(username));
            return people;


    }

    private void savingUsernames() throws IOException {
        //Save the usernames in file
        if(!usernames.contains(System.getProperty("user.name"))) {
            usernames.add(System.getProperty("user.name"));
        }
       PrintWriter printWriter = new PrintWriter("Users.txt");
        for (int i = 0; i < usernames.size(); i++) {
            printWriter.println(usernames.get(i));
        }

        printWriter.close();





    }

}
