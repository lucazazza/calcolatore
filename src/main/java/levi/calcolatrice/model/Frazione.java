package levi.calcolatrice.model;

/**
 *
 * denominatore sempre positivo
 * lo zero è rappresentato dalla frazione 0/1
 */

public class Frazione {
    private long numeratore;
    private long denominatore;
    static boolean dec = false;

    public Frazione(long numeratore, long denominatore) throws ArithmeticException{
        if(denominatore == 0){
            throw new ArithmeticException("Non definita frazione con denominatore uguale a 0");
        }
        if(denominatore < 0){
            numeratore *= -1;
            denominatore *= -1;
        }
        if(numeratore == 0){
            this.numeratore = 0;
            this.denominatore = 1;
        }else{
            long mcd = massimoComuneDivisore(numeratore, denominatore);
            this.numeratore = numeratore / mcd;
            this.denominatore = denominatore / mcd;
        }
        dec = false;
    }

    public static void cambiaRisultato() {
        dec = !dec;
    }

    @Override
    public String toString(){
        if(denominatore == 1){
            return Long.toString(numeratore);
        }
        if(dec){
            return Double.toString((double) numeratore / denominatore);
        }
        return numeratore + "/" + denominatore;
    }

    private static long massimoComuneDivisore(long a1, long b1){
        long a = Math.abs(a1);
        long b = Math.abs(b1);
        while(a != b){
            if(a > b)
                a -= b;
            else
                b -= a;
        }
        return a;
    }

    private static long minimoComuneMultiplo(long n1, long n2){
        long mcd = massimoComuneDivisore(n1, n2);
        return (n1*n2)/mcd;
    }

    /**
     * Moltiplicazione
     * @param f secondo operando
     * @return this * f
     */
    public Frazione mult(Frazione f){
        return new Frazione(this.numeratore * f.numeratore, this.denominatore * f.denominatore);
    }

    public Frazione div(Frazione f){
        return this.mult(new Frazione(f.denominatore, f.numeratore));
    }

    public Frazione add(Frazione f){
        long num, den;
        den = minimoComuneMultiplo(this.denominatore, f.denominatore);
        num = den / this.denominatore * this.numeratore + den / f.denominatore * f.numeratore;
        return new Frazione(num, den);
    }

    public Frazione sott(Frazione f){
        long num, den;
        den = minimoComuneMultiplo(this.denominatore, f.denominatore);
        num = den / this.denominatore * this.numeratore - den / f.denominatore * f.numeratore;
        return new Frazione(num, den);
    }

    public Frazione pow(Frazione f){
        long num, den;
        den = (long) Math.pow(this.denominatore, f.numeratore);
        num = (long) Math.pow(this.numeratore, f.numeratore);
        return new Frazione(num, den);
    }


}