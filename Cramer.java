import java.math.BigInteger;
import java.util.Scanner;
import java.util.Stack;

public class Cramer {
    
    final static Scanner ler = new Scanner(System.in);
    
	public static void main(String[] args) {
	    int N = ler.nextInt();
	    ler.nextLine();
		BigFraction sistema[][] = lerMatriz(N, N);
		BigFraction resultado[] = lerVetor(N);
		System.out.println();
		imprimirMatriz(sistema);
		System.out.println();
		imprimirVetor(resultado);
		System.out.println();
		
		BigFraction solucoes[] = cramer(sistema, resultado);
		imprimirSolucoes(solucoes);
	}
	
	public static BigFraction[][] lerMatriz(int N, int M) {
	    BigFraction matriz[][] = new BigFraction[N][M];
	    
	    Stack<String> aux = new Stack<>();
	    while (aux.size() < N*M) {
	        String linha[] = ler.nextLine().split(" ");
	        
	        for (int j = 0; j < linha.length; j++) {
	            aux.add(0, linha[j]);
	        }
	    }
	    
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < M; j++) {
	            if (!aux.isEmpty()) matriz[i][j] = leitura(aux.pop());
	            else matriz[i][j] = leitura("faltaram itens");
	        }
	    }
	    
	    return matriz;
	}
	
	public static BigFraction[] lerVetor(int N) {
	    BigFraction vetor[] = new BigFraction[N];
	    
	    Stack<String> aux = new Stack<>();
	    while (aux.size() < N) {
	        String linha[] = ler.nextLine().split(" ");
	        for (int j = 0; j < linha.length; j++) {
	            aux.add(0, linha[j]);
	        }
	    }
	    
	    for (int i = 0; i < N; i++) {
	        vetor[i] = leitura(aux.pop());
	    }
	    
	    return vetor;
	}
	
	public static BigFraction leitura(String elemento) {
	    if (!elemento.matches("[-]?[0-9]*/[0-9]*|[-]?[0-9]*")) {
            return new BigFraction("0/1");
        } else if (elemento.contains("/")) {
            return new BigFraction(elemento);
        } else {
            return new BigFraction(new BigInteger(elemento), BigInteger.ONE);
        }
	}
	
	public static BigFraction[] cramer(BigFraction sistema[][], BigFraction resultados[]) {
	    BigFraction determinante = chio(copiarMatriz(sistema), new BigFraction("1/1"));
	    
	    if (determinante.a.equals(BigInteger.ZERO)) return null;
	    
	    BigFraction solucoes[] = new BigFraction[sistema.length];
	    for (int i = 0; i < solucoes.length; i++) {
	        BigFraction sistemaAlt[][] = substituirColuna(sistema, resultados, i);
	        imprimirMatriz(sistemaAlt);
	        System.out.println();
	        solucoes[i] = chio(copiarMatriz(sistemaAlt), new BigFraction("1/1"));
	        imprimirFracao(solucoes[i]);
	        System.out.println();
	        solucoes[i].dividirFracao(determinante);
	    }

	    return solucoes;
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
    
    public static BigFraction[][] copiarMatriz(BigFraction x[][]) {
        BigFraction y[][] = new BigFraction[x.length][x[0].length];
        
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                y[i][j] = new BigFraction(x[i][j].a, x[i][j].b);
            }
        }
        
        return y;
    }
    
    public static BigFraction[][] substituirColuna(BigFraction matriz[][], BigFraction coluna[], int c) {
        BigFraction matrizNova[][] = new BigFraction[matriz.length][matriz[0].length];
        
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (j == c) matrizNova[i][j] = new BigFraction(coluna[i].a, coluna[i].b);
                else matrizNova[i][j] = new BigFraction(matriz[i][j].a, matriz[i][j].b);
            }
        }
        return matrizNova;
    }
	
	public static void imprimirMatriz(BigFraction matriz[][]) {
	    for (int i = 0; i < matriz.length; i++) {
	        for (int j = 0; j < matriz[0].length; j++) {
	            imprimirFracao(matriz[i][j]);
	            System.out.print(" ");
	        }
	        System.out.println();
	    }
	}
	
	public static void imprimirVetor(BigFraction vetor[]) {
	    for (int i = 0; i < vetor.length; i++) {
	        imprimirFracao(vetor[i]);
	        System.out.print(" ");
	    }
	}
	
	public static void imprimirSolucoes(BigFraction solucoes[]) {
	    for (int i = 0; i < solucoes.length; i++) {
	        System.out.printf("X%d: ", i+1);
	        imprimirFracao(solucoes[i]);
	        System.out.println();
	    }
	}
	
	public static void imprimirFracao(BigFraction fracao) {
	    if (fracao.b.equals(BigInteger.ONE) || fracao.a.equals(BigInteger.ZERO)) System.out.printf("%s ", fracao.a);
	    else System.out.printf("%s/%s", fracao.a, fracao.b);
	}
}
