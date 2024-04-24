import java.math.BigInteger;

public class BigFraction {
    BigInteger a, b;
    final static BigInteger minusOne = new BigInteger("-1");
    
    public BigFraction(BigInteger a, BigInteger b) {
        this.a = a;
        this.b = b;
        atualizarPropriedades();
    }
    
    public BigFraction(String fraction) {
        String fracao[] = fraction.split("/");
        this.a = new BigInteger(fracao[0]);
        this.b = new BigInteger(fracao[1]);
        atualizarPropriedades();
    }
    
    public void atualizarPropriedades() {
        BigInteger maiorDiv = a.gcd(b);
        this.a = a.divide(maiorDiv);
        this.b = b.divide(maiorDiv);
        
        int condicao1 = a.compareTo(BigInteger.ZERO);
        int condicao2 = b.compareTo(BigInteger.ZERO);
        if ((condicao1 == -1 && condicao2 == -1) || (condicao1 == 1 && condicao2 == -1)) {
            a = a.multiply(minusOne);
            b = b.multiply(minusOne);
        }
    }
    
    public void multiplicarInteiro(BigInteger x) {
        this.a = a.multiply(x);
        atualizarPropriedades();
    }
    
    public void multiplicarFracao(BigFraction x) {
        this.a = a.multiply(x.a);
        this.b = b.multiply(x.b);
        atualizarPropriedades();
    }
    
    public void dividirInteiro(BigInteger x) {
        this.b = b.multiply(x);
        atualizarPropriedades();
    }
    
    public void dividirFracao(BigFraction x) {
        this.a = a.multiply(x.b);
        this.b = b.multiply(x.a);
        atualizarPropriedades();
    }
    
    public void adicionarInteiro(BigInteger x) {
        this.a = this.a.add(x.multiply(b));
        atualizarPropriedades();
    }
    
    public void adicionarFracao(BigFraction x) {
        this.a = (a.multiply(x.b)).add(x.a.multiply(b));
        this.b = b.multiply(x.b);
        
        atualizarPropriedades();
    }
    
    public void subtrairInteiro(BigInteger x) {
        this.a = a.subtract(x.multiply(b));
        atualizarPropriedades();
    }
    
    public void subtrairFracao(BigFraction x) {
        this.a = (a.multiply(x.b)).subtract(x.a.multiply(b));
        this.b = b.multiply(x.b);
        
        atualizarPropriedades();
    }
}