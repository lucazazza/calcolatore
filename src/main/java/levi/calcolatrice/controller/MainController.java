package levi.calcolatrice.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import levi.calcolatrice.model.*;

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
    Frazione risultato;
    public void initialize(){
        dati.add("");
        operazione.setText("");
        back.setDisable(true);
    }

    public void modifica(ActionEvent actionEvent) {
        String n = ((Button)actionEvent.getSource()).getText();
        switch(dati.getLast()){
            case "+", "-", "*", "/", "", "(":
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

    public void risolvi(){
        String s = "";
        for(int i = 0; i < dati.size(); i++){
            s += dati.get(i);
        }
        Espressione e = new Espressione(s);
        try{
            risultato = e.risultato();
            operazione.setText(risultato.toString());
            dati.clear();
            dati.add(operazione.getText());
        }catch(ExpressionException|ArithmeticException ex){
            operazione.setText(ex.getMessage());
        }
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

    public void inputTastiera(KeyEvent keyEvent) throws ExpressionException {
        KeyCode c = keyEvent.getCode();
        System.out.println(c);

        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT7){
            dati.add(Operatore.DIV.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT8){
            dati.add(Parentesi.PARENTESI_APERTA.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT9){
            dati.add(Parentesi.PARENTESI_CHIUSA.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.PLUS){
            dati.add(Operatore.MULT.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && keyEvent.getText().equals("ì")){
            dati.add(Operatore.POW.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && keyEvent.getText().equals("0")){
            risolvi();
            return;
        }

        switch(c){
            case DIGIT0, DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9,
                 NUMPAD0,NUMPAD1,NUMPAD2,NUMPAD3,NUMPAD4,NUMPAD5,NUMPAD6,NUMPAD7,NUMPAD8,NUMPAD9:
                dati.add(keyEvent.getText());
                aggiorna();
                break;
            case MINUS, SUBTRACT:
                dati.add(Operatore.SUB.toString());
                aggiorna();
                break;
            case PLUS, ADD:
                dati.add(Operatore.ADD.toString());
                aggiorna();
                break;
            case DIVIDE:
                dati.add(Operatore.DIV.toString());
                aggiorna();
                break;
            case MULTIPLY:
                dati.add(Operatore.MULT.toString());
                aggiorna();
                break;
            case BACK_SPACE:
                indietro();
                break;
            default:
                break;
        }

    }

}