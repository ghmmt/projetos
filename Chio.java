import java.util.Random;
import java.math.BigInteger;

public class Chio {
    
    final static Random gerador = new Random();
    
	public static void main(String[] args) {
		int N = 100;
		BigFraction matriz[][] = gerarMatriz(N);
		imprimirMatriz(matriz);
		System.out.println();
		BigFraction determinante = chio(matriz, new BigFraction("1/1"));
		System.out.printf("%s/%s\n", determinante.a, determinante.b);
	}
	
	public static BigFraction[][] gerarMatriz(int N) {
	    BigFraction matriz[][] = new BigFraction[N][N];
	    
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            matriz[i][j] = new BigFraction(new BigInteger(gerador.nextInt(11)+""), new BigInteger((gerador.nextInt(10)+1)+""));
	        }
	    }
	    
	    return matriz;
	}
	
	public static void imprimirMatriz(BigFraction matriz[][]) {
	    for (int i = 0; i < matriz.length; i++) {
	        for (int j = 0; j < matriz[0].length; j++) {
	            if (matriz[i][j].b.equals(BigInteger.ONE) || matriz[i][j].a.equals(BigInteger.ZERO)) System.out.printf("%s ", matriz[i][j].a);
	            else System.out.printf("%s/%s ", matriz[i][j].a, matriz[i][j].b);
	        }
	        System.out.println();
	    }
	}
	
	public static BigFraction chio(BigFraction matriz[][], BigFraction modificacoesProduto) {
	    if (matriz == null) return new BigFraction("0/0");
	    else if (determinanteEqualsZero(matriz)) return new BigFraction("0/1");
	    else if (matriz.length == 1) {
	        matriz[0][0].multiplicarFracao(modificacoesProduto);
	        return matriz[0][0];
	    }
	    
        if (matriz[0][0].a.equals(BigInteger.ZERO)) {
	        modificacoesProduto.a = modificacoesProduto.a.multiply(BigFraction.minusOne);
	        
	        for (int i = 1; i < matriz.length; i++) {
	            if (!matriz[i][0].a.equals(BigInteger.ZERO)) {
	                BigFraction aux[] = matriz[i];
	                matriz[i] = matriz[0];
	                matriz[0] = aux;
	                break;
	            }
	        }
	    }
	    
	    if (!matriz[0][0].a.equals(BigInteger.ONE)) {
    	    modificacoesProduto.multiplicarFracao(matriz[0][0]);
    	    BigFraction div = new BigFraction(matriz[0][0].b, matriz[0][0].a);
            for (int i = matriz[0].length - 1; i >= 0; i--) {
                matriz[0][i].multiplicarFracao(div);
            }
	    }
	    
	    BigFraction matrizReduzida[][] = new BigFraction[matriz.length - 1][matriz[0].length - 1];
	    for (int i = 1; i < matriz.length; i++) {
	        for (int j = 1; j < matriz[0].length; j++) {
	            matrizReduzida[i-1][j-1] = new BigFraction(matriz[i][j].a, matriz[i][j].b);
	            BigFraction aux = new BigFraction(matriz[i][0].a, matriz[i][0].b);
	            aux.multiplicarFracao(matriz[0][j]);
	            matrizReduzida[i-1][j-1].subtrairFracao(aux);
	        }
	    }
	    
	    return chio(matrizReduzida, modificacoesProduto);
	}
	
	/*public static BigFraction sarrus(BigFraction matriz[][]) {
	    BigFraction determinante = new BigFraction("0/1");
	    
	    for (int i = 0; i < 3; i++) {
	        BigFraction aux = new BigFraction(matriz[0][i].a, matriz[0][i].b);
	        aux.multiplicarFracao(matriz[1][(i + 1) % 3]);
	        aux.multiplicarFracao(matriz[2][(i + 2) % 3]);
	        determinante.adicionarFracao(aux);
	    }
	    
	    for (int i = 0; i < 3; i++) {
	        BigFraction aux = new BigFraction(matriz[0][(i + 2) % 3].a, matriz[0][(i + 2) % 3].b);
	        aux.multiplicarFracao(matriz[1][(i + 1) % 3]);
	        aux.multiplicarFracao(matriz[2][i]);
	        determinante.subtrairFracao(aux);
	    }
	    
	    return determinante;
	}*/
	
	public static boolean determinanteEqualsZero(BigFraction matriz[][]) {
	    int sum = 0;
	    for (int j = 0; j < matriz[0].length; j++) {
	        for (int i = 0; i < matriz.length; i++) {
	            if (matriz[i][j].a.equals(BigInteger.ZERO)) sum++;
	        }
	        
	        if (sum == matriz.length) return true;
	        sum = 0;
	    }
	    
	    BigFraction matrizT[][] = transporMatriz(matriz);
	    for (int j = 0; j < matrizT[0].length; j++) {
	        for (int i = 0; i < matrizT.length; i++) {
	            if (matrizT[i][j].a.equals(BigInteger.ZERO)) sum++;
	        }
	        
	        if (sum == matrizT.length) return true;
	        sum = 0;
	    }
	    
	    return false;
	}
	
	public static BigFraction[][] transporMatriz(BigFraction matriz[][]) {
        if (matriz == null) return null;
        
        BigFraction matrizTransposta[][] = new BigFraction[matriz[0].length][matriz.length];

        for (int i = 0; i < matrizTransposta.length; i++) {
            for (int j = 0; j < matrizTransposta[0].length; j++) {
                matrizTransposta[i][j] = matriz[j][i];               
            }
        }

        return matrizTransposta;
    }
}