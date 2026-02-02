package levi.calcolatrice.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import levi.calcolatrice.model.Espressione;
import levi.calcolatrice.model.ExpressionException;

import java.util.ArrayList;

public class MainController {
    @FXML
    public Label operazione;
    @FXML
    public Button b0;
    public Button b1;
    public Button b2;
    public Button b3;
    public Button b4;
    public Button b5;
    public Button b6;
    public Button b7;
    public Button b8;
    public Button b9;
    public Button eq;
    public Button addiz;
    public Button sott;
    public Button molt;
    public Button div;
    public Button back;
    public Button clearBtn;
    public Button pow;
    public Button parAperta;
    public Button parChiusa;


    private ArrayList<String> dati = new ArrayList<>();

    public void initialize(){
        dati.add("");
        operazione.setText("");
        back.setDisable(true);
    }

    public void modifica(ActionEvent actionEvent) {
        String n = ((Button)actionEvent.getSource()).getText();
        switch(dati.getLast()){
            case "+", "-", "x", ":", "", "(":
                if(n.equals("+") || n.equals("-") || n.equals("x") || n.equals(":") || n.equals("^") || n.equals(")")){
                    break;
                }
                dati.add(n);
                break;
            default:
                dati.add(n);
                break;
        }
        aggiorna();
    }

    public void risolvi() throws ExpressionException {
        Espressione e = new Espressione(operazione.getText());
        operazione.setText(e.calcRPN().toString());
        dati.clear();
        dati.add(operazione.getText());
    }

    public void indietro(){
        dati.removeLast();
        aggiorna();
    }

    public void cancella(){
        dati.removeAll(dati);
        dati.add("");
        aggiorna();
    }

    public void aggiorna(){
        String s = "";
        for(String n : dati){
            s += n;
        }
        operazione.setText(s);
        if(dati.size() > 1){
            back.setDisable(false);
        }else{
            back.setDisable(true);
        }

    }
}