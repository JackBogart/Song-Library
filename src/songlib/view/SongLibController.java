package songlib.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import songlib.app.Song;

/*
 * SongLibController
 * John Bogart & Connor Holm
 * Rutgers University
 * 2023
 */

public class SongLibController {

    @FXML
    Button add;
    @FXML
    Button edit;
    @FXML
    Button delete;
    @FXML
    TextField songField;
    @FXML
    TextField artistField;
    @FXML
    TextField albumField;
    @FXML
    TextField yearField;
    @FXML
    Text songartist;
    @FXML
    Text albumyear;
    @FXML
    ListView<Song> listView;

    private ObservableList<Song> obsSet;
    TreeSet<Song> ts = new TreeSet<Song>();

    public void start(Stage mainStage) throws IOException {

        // LOAD LISTENERS
        listView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Song>() {
                    public void changed(ObservableValue<? extends Song> observable,
                            Song oldSong, Song newSong) {
                        if (newSong == null)
                            return;
                        updateSelection(newSong);
                    }
                });

        mainStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            FileWriter writer = new FileWriter("src/data/data.txt");
                            BufferedWriter bwr = new BufferedWriter(writer);
                            for (int i = 0; i < ts.size(); i++) {
                                bwr.write(obsSet.get(i).getName());
                                bwr.write("|");
                                bwr.write(obsSet.get(i).getArtist());
                                bwr.write("|");
                                bwr.write(obsSet.get(i).getAlbum());
                                bwr.write("|");
                                bwr.write(obsSet.get(i).getYear());
                                bwr.write("\n");
                            }
                            bwr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.exit(0);
                    }
                });
            }
        });

        // READ FROM data.txt
        try {
            FileReader textFileReader = new FileReader("src/data/data.txt");
            BufferedReader reader = new BufferedReader(textFileReader);
            String line;

            // create ObservableList from Treeset
            if ((line = reader.readLine()) != null) { // if data exists
                String[] arr;
                do {
                    arr = line.split("\\|", -1);
                    Song newSong = new Song(arr[0], arr[1], arr[2], arr[3]);
                    ts.add(newSong);

                } while ((line = reader.readLine()) != null);
                // Conditional to do this only if previous songs are available from file
                obsSet = FXCollections.observableArrayList(ts);

                listView.setItems(obsSet);

                // select the first item
                listView.getSelectionModel().select(0);
            } else {
                obsSet = FXCollections.observableArrayList();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void add(ActionEvent e) {
        if (confirm())
            return;
        // Empty song or artist textfield
        if (songField.getText().equals("") || artistField.getText().equals("")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't add song");
            alert.setContentText("Song or Artist field is empty");

            alert.showAndWait();
            return;
        }

        // Checks that '|' is not contained in fields
        if (songField.getText().contains("|") || artistField.getText().contains("|")
                || albumField.getText().contains("|")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't add song");
            alert.setContentText("Invalid Charatcer: '|'");

            alert.showAndWait();
            return;
        }

        // Ensures year is a number
        String year = "";
        if (isNumeric(yearField.getText().trim()))
            year += yearField.getText().trim();
        else
            return;

        Song newSong = new Song(songField.getText().trim(), artistField.getText().trim(), albumField.getText().trim(),
                year);
        // If song doesn't exist, add to set
        if (ts.add(newSong)) {
            update();
            listView.getSelectionModel().select(obsSet.indexOf(newSong)); // SELECT AFTER ADD
        }
        // Song exists
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't add song");
            alert.setContentText("Song already exists");

            alert.showAndWait();
        }
    }

    public void edit(ActionEvent e) {
        if (confirm())
            return;
        // ERROR CASE: List contains 0 songs.
        if (ts.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't edit song");
            alert.setContentText("List does not contain any songs");

            alert.showAndWait();
            return;
        }

        // ERROR CASE: song text field or artist text field is empty.
        if (songField.getText().equals("") || artistField.getText().equals("")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't edit song");
            alert.setContentText("Song or Artist field is empty");

            alert.showAndWait();
            return;
        }

        // Checks that '|' is not contained in fields
        if (songField.getText().contains("|") || artistField.getText().contains("|")
                || albumField.getText().contains("|")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't edit song");
            alert.setContentText("Invalid Charatcer: '|'");

            alert.showAndWait();
            return;
        }

        // Get edited text from text fields.
        String newSongField = songField.getText().trim();
        String newArtistField = artistField.getText().trim();
        String newAlbumField = albumField.getText().trim();

        // Ensures year is a number
        String newYearField = "";
        if (isNumeric(yearField.getText().trim()))
            newYearField += yearField.getText().trim();
        else
            return;
        Song editedSong = new Song(newSongField, newArtistField, newAlbumField, newYearField);
        Song song = listView.getSelectionModel().getSelectedItem();

        // check if song is already in list and if its the selected song
        if (ts.contains(editedSong) && editedSong.compareTo(song) != 0) {
            // ERROR CASE: Edited song already exists in list elsewhere.
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't edit song");
            alert.setContentText("Edited song already exists in list");

            alert.showAndWait();
            return;

        }
        // Edit list
        else {
            ts.remove(song);
            ts.add(editedSong);
            update();
            listView.getSelectionModel().select(obsSet.indexOf(editedSong)); // SELECT AFTER EDIT
        }
    }

    public void delete(ActionEvent e) {
        if (confirm())
            return;
        // ERROR CASE: List contains 0 songs.
        if (ts.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Can't delete song");
            alert.setContentText("List does not contain any songs");

            alert.showAndWait();
            return;
        }
        // Delete currently selected song in ListView from treeSet, then update list
        Song song = listView.getSelectionModel().getSelectedItem();
        int index = obsSet.indexOf(song);
        ts.remove(song);
        update();
        // determine deleted song location
        if (ts.size() != 0) {
            // select next song
            if (index != ts.size()) {
                listView.getSelectionModel().select(index);
            }
            // select previous song
            else {
                listView.getSelectionModel().select(index - 1);

            }
        }
        // If song was last in list, make text fields blank
        else {
            albumyear.setText("");
            songField.setText("");
            artistField.setText("");
            albumField.setText("");
            yearField.setText("");
            songartist.setText("");
        }
    }

    // HELPER METHODS
    // Updates list
    private void update() {
        obsSet.clear();
        obsSet.addAll(ts);
        listView.setItems(obsSet);
    }

    // updates text fields for new selection from list
    private void updateSelection(Song info) {
        songartist.setText(info.getConcatenate());
        String albyear = "";
        if (!info.getAlbum().equals("")) {
            albyear += info.getAlbum() + " ";
        }

        if (!info.getYear().equals("")) {
            albyear += "(" + info.getYear() + ")";
        }
        albumyear.setText(albyear);
        songField.setText(info.getName());
        artistField.setText(info.getArtist());
        albumField.setText(info.getAlbum());
        yearField.setText(info.getYear());
    }

    // generic confirmation box
    private boolean confirm() {
        Alert confirm = new Alert(AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();
        return (confirm.getResult() == ButtonType.CANCEL);
    }

    // checks that year is numeric
    private boolean isNumeric(String strNum) {
        if (strNum.equals("")) {
            return true;
        }
        try {
            int d = Integer.parseInt(strNum);
            if (d <= 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Error: Invalid input");
                alert.setContentText("Year cannot be negative");

                alert.showAndWait();
                return false;
            }
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error: Invalid input");
            alert.setContentText("Year must be an integer");

            alert.showAndWait();
            return false;
        }
        return true;
    }
}
