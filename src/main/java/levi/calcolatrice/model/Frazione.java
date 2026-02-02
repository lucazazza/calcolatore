package levi.calcolatrice.model;

/**
 *
 * denominatore sempre positivo
 * lo zero è rappresentato dalla frazione 0/1
 */

public class Frazione {
    private long numeratore;
    private long denominatore;

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
            long mcd = massimoComunDenominatore(numeratore, denominatore);
            this.numeratore = numeratore / mcd;
            this.denominatore = denominatore / mcd;
        }

    }
    @Override
    public String toString(){
        if (denominatore == 1){
            return Long.toString(numeratore);
        }
        return "(" + numeratore + "/" + denominatore + ")";
    }

    private static long massimoComunDenominatore(long a, long b) {
        long a1 = Math.abs(a);
        long b1 = Math.abs(b);
        while (a1 != b1) {
            if (a1 > b1) {
                a1 -= b1;
            } else if (b1 > a1) {
                b1 -= a1;
            }
        }
        return a1;
    }

    private Frazione opposto() {
        return new Frazione(numeratore * -1, denominatore);
    }

    private static long minimoComuneMultiplo(long a, long b) {
        long a1 = Math.abs(a);
        long b1 = Math.abs(b);
        return (a1 * b1) / massimoComunDenominatore(a1, b1);
    }

    public Frazione mult(Frazione f){
        return new Frazione(this.numeratore * f.numeratore, this.denominatore * f.denominatore);
    }
    public Frazione div(Frazione f){
        return this.mult(new Frazione(f.denominatore, f.numeratore));
    }
    public Frazione add(Frazione f){
        long num, den;
        den = massimoComunDenominatore(this.denominatore, f.denominatore);
        num = den / this.denominatore * this.numeratore + den / f.denominatore * f.numeratore;
        return new Frazione(num, den);
    }

    public Frazione sum(Frazione f) {
        long mcm = minimoComuneMultiplo(denominatore, f.denominatore);
        long num = numeratore * (mcm / denominatore) + f.numeratore * (mcm / f.denominatore);
        return new Frazione(num, mcm);
    }

    public Frazione pow(Frazione exp) {
        Frazione f = null;
        if (exp.numeratore == 0)
            if (this.numeratore == 0)
                throw new ArithmeticException("0^0 non definito");
            else
                f = new Frazione(1, 1);
        else if (exp.denominatore != 1) {
            throw new ArithmeticException("Non implementato calcolo di potenza con esponente non intero.");
        } else if (exp.numeratore > 0)
            f = new Frazione((long) Math.pow(this.numeratore, exp.numeratore),
                    (long) Math.pow(this.denominatore, exp.numeratore));
        else
            f = new Frazione((long) Math.pow(this.denominatore, -exp.numeratore),
                    (long) Math.pow(this.numeratore, -exp.numeratore));
        return f;
    }
    private static Frazione opposto(Frazione f) {
        return new Frazione(-1 * f.numeratore, f.denominatore);
    }

    public Frazione sub(Frazione f) {
        return this.sum(opposto(f));
    }


}
