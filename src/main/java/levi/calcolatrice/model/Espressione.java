package levi.calcolatrice.model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Espressione {
    private String inputExpr;
    ArrayList<Object> tokensList;
    ArrayList<Object> validTokensList;
    private ArrayList<Object> rpn;

    ArrayList<Object> output = new ArrayList<>();
    Stack<Object> operatori = new Stack<>();

    public Espressione(String inputExpr) {
        tokensList = new ArrayList<>();
        validTokensList = new ArrayList<>();
        this.inputExpr = inputExpr;
    }

    public void scanner() throws ExpressionException {
        long numero = 0;
        boolean inLetturaNumero = false;
        for (char carattere : inputExpr.toCharArray()) {
            switch (carattere) {
                case '(':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Parentesi.PARENTESI_APERTA);
                    break;
                case ')':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Parentesi.PARENTESI_CHIUSA);
                    break;
                case '+':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Operatore.ADD);
                    break;
                case '-':
                    if (inLetturaNumero) {
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Operatore.SUB);
                    break;
                case '*':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Operatore.MULT);
                    break;
                case '/':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Operatore.DIV);
                    break;
                case '^':
                    if (inLetturaNumero){
                        tokensList.add(new Frazione(numero, 1));
                        inLetturaNumero = false;
                        numero = 0;
                    }
                    tokensList.add(Operatore.POW);
                    break;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    numero = (numero * 10) + Long.parseLong(Character.toString(carattere));
                    inLetturaNumero = true;
                    break;
                default:
                    throw new ExpressionException("errore nella lettura dello scanner");

            }
        }
        if (inLetturaNumero){
            tokensList.add(new Frazione(numero, 1));
        }
        System.out.println(tokensList);
    }

    public void parser() throws ExpressionException {
        /*
            stato = 0: in attesa di espressione
            stato = 1: letto Operatore
            stato = 2: letto Operando (Frazione)
            stato = 3: letta Parentesi Chiusa
         */
        scanner();
        int stato = 0;
        boolean operatoreUsato = false;
        for (Object token : tokensList) {
            switch (stato) {
                case 0:
                    /*-- stato 0 ----- in attesa di espressione -------------------------------*/
                    if (token instanceof Operatore) {
                        if (operatoreUsato){
                            throw new ExpressionException("errore nel parser");
                        }
                        switch ((Operatore)token){
                            case ADD:
                                operatoreUsato = true;
                                break;
                            case SUB:
                                operatoreUsato = true;
                                validTokensList.add(new Frazione(-1,1));
                                validTokensList.add(Operatore.MULT);
                                break;
                            default:
                                throw new ExpressionException("errore nel parser");

                        }
                        stato = 1;
                    } else if (token instanceof Parentesi) {
                        operatoreUsato = false;
                        if(token.equals(Parentesi.PARENTESI_CHIUSA)){
                            throw new ExpressionException("errore nel parser");
                        }else{
                            validTokensList.add(token);
                        }
                    } else if (token instanceof Frazione) {
                        operatoreUsato = false;
                        validTokensList.add(token);
                        stato = 2;
                    }

                    break;
                case 1:
                    /*-- stato 1 ----- letto Operatore -----------------------------*/
                    if (token instanceof Operatore) {
                        if((token.equals(Operatore.ADD) || token.equals(Operatore.SUB)) && !operatoreUsato && ((Operatore)tokensList.getLast()).getPriorita()==3){
                            operatoreUsato = true;
                            if(token.equals(Operatore.SUB)){
                                validTokensList.add(new Frazione(-1,1));
                                validTokensList.add(Operatore.MULT);
                            }
                        }
                        throw new ExpressionException("errore nel parser");
                    } else if (token instanceof Frazione) {
                        validTokensList.add(token);
                        stato = 2;
                        operatoreUsato = false;
                    } else if (token instanceof Parentesi) {
                        operatoreUsato = false;
                        if(token.equals(Parentesi.PARENTESI_CHIUSA)){
                            throw new ExpressionException("errore nel parser");
                        }
                        validTokensList.add(token);
                        stato = 0;
                    }
                    break;
                case 2:
                    /*-- stato 2 ----- letto Operando (Frazione) -----------------------------------------*/
                    if (token instanceof Operatore) {
                        validTokensList.add(token);
                        stato = 1;
                    } else if (token instanceof Frazione) {
                        throw new ExpressionException("errore nel parser");
                    } else if (token instanceof Parentesi) {
                        if(token.equals(Parentesi.PARENTESI_APERTA)){
                            throw new ExpressionException("errore nel parser");
                        }
                        validTokensList.add(token);
                        stato = 3;
                    }
                    break;
                case 3:
                    /*-- stato 3 ----- letta Parentesi Chiusa ---------------------------------------------*/
                    if (token instanceof Operatore) {
                        validTokensList.add(token);
                        stato = 1;
                    } else if (token instanceof Frazione) {
                        validTokensList.add(Operatore.MULT);
                        validTokensList.add(token);
                    } else if (token instanceof Parentesi) {
                        if(token.equals(Parentesi.PARENTESI_APERTA)){
                            validTokensList.add(Operatore.MULT);
                            stato = 0;
                        }
                        validTokensList.add(token);
                    }
            }
        }
        //non deve terminare con un operatoreUsato (stato 1)
        if (stato == 1)
            throw new ExpressionException("errore nel parser");

        System.out.println(validTokensList);
    }

    public void shuntingYards() throws ExpressionException {
        parser();
        for(Object token : validTokensList){
            if(token instanceof Frazione){
                output.add(token);
            }else if(token instanceof Operatore){
                while(!operatori.isEmpty() && operatori.peek() instanceof Operatore){
                    if(((Operatore)operatori.peek()).getPriorita() >= ((Operatore)token).getPriorita()){
                        output.add(operatori.pop());
                    }else{
                        break;
                    }
                }
                operatori.push(token);
            } else if(token == Parentesi.PARENTESI_APERTA){
                operatori.push(token);
            } else if(token == Parentesi.PARENTESI_CHIUSA){
                while(!(operatori.peek().equals(Parentesi.PARENTESI_APERTA))){
                    output.add(operatori.pop());
                }
                operatori.pop();
            }
        }
        while(!operatori.isEmpty()){
            if(operatori.peek() instanceof Operatore){
                output.add(operatori.pop());
            }else{
                throw new ExpressionException("errore nello shunting yard");
            }
        }
        rpn = output;
        System.out.println(rpn);
    }

    public Frazione risultato() throws ExpressionException {
        shuntingYards();
        Stack<Frazione> output = new Stack<>();
        for(Object o : rpn){
            if(o instanceof Frazione){
                output.push((Frazione)o);
            }else{
                try{
                    Frazione n2 = output.pop();
                    Frazione n1 = output.pop();
                    switch ((Operatore)o){
                        case ADD ->  output.push(n1.add(n2));
                        case SUB -> output.push(n1.sott(n2));
                        case MULT -> output.push(n1.mult(n2));
                        case DIV -> output.push(n1.div(n2));
                        case POW -> output.push(n1.pow(n2));
                    }
                }catch (EmptyStackException e){
                    throw new ExpressionException("errore nel calcolo");
                }
            }
        }
        Frazione risultato = output.pop();
        System.out.println(risultato);
        return risultato;
    }
}