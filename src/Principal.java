/*
 * Universidade Federal de Santa Catarina - UFSC
 * Departamento de Informática e Estatística - INE
 * Programa de Pós-Graduação em Ciências da Computação - PROPG
 * Disciplinas: Projeto e Análise de Algoritmos
 * Prof Alexandre Gonçalves da Silva 
 *
 * Baseado nos slides 38 da aula do dia 27/10/2017  
 *
 * Página 312 Thomas H. Cormen 3a Ed 
 *
 * Código de Huffman com palavra de comprimento variável.
 */

/**
 * @author Osmar de Oliveira Braz Junior
 */
import java.util.PriorityQueue;

public class Principal {

    //Tamanho do alfabeto do ASCII extendido
    static final int R = 256;
    //Qtde caracteres codificados
    static int Rn;

    // No da árvore binária dos códigos de Huffman
    // A classe precisa implementar Comparable devido a fila de prioridade
    private static class No implements Comparable<No> {

        private final char chave;
        private final int frequencia;
        private final No esquerda, direita;

        No(char chave, int frequencia, No esquerda, No direita) {
            this.chave = chave;
            this.frequencia = frequencia;
            this.esquerda = esquerda;
            this.direita = direita;
        }

        /**
         * Verifica se um nó é folha
         */
        private boolean eFolha() {
            assert ((esquerda == null) && (direita == null)) || ((esquerda != null) && (direita != null));
            return (esquerda == null) && (direita == null);
        }

        /**
         * Compara baseado na frequencia
         */
        public int compareTo(No no) {
            return this.frequencia - no.frequencia;
        }
    }

    /**
     * Mostra a tabela de frequência.
     *
     * @param f Tabela de frequencia.
     */
    public static void mostrarFrequencia(int[] f) {
        int n = f.length;
        for (char i = 0; i < n; i++) {
            if (f[i] != 0) {
                System.out.println(i + " = " + f[i] + " ");
            }
        }
    }

    /**
     * Mostra a tabela de símbolos.
     *
     * @param f Tabela de símbolo.
     */
    public static void mostrarTabelaSimbolos(String[] tabelaSimbolo) {
        for (char i = 0; i < R; i++) {
            if (tabelaSimbolo[i] != null) {
                //Mostra o código do tamanho variável
                int inicio = tabelaSimbolo[i].indexOf("1");
                String codigo;
                if (inicio == -1) {
                    codigo = "0";
                } else {
                    codigo = tabelaSimbolo[i].substring(inicio, tabelaSimbolo[i].length());
                }
                System.out.println(i + " = " + codigo + " ");
                //System.out.println(i + " = "+ tabelaSimbolo[i].substring(tabelaSimbolo[i].length()-Rn+1, tabelaSimbolo[i].length() ) + " ");
            }
        }
    }

    /**
     * Conta a frequência de cada caracter.
     *
     * @param caracteres vetor de caracteres
     * @return um hashmap como a chave o caracter e a frequencia do caracter
     */
    public static int[] frequencia(char[] caracteres) {
        int n = caracteres.length;
        int[] frequencia = new int[R];
        for (int i = 0; i < n; i++) {
            frequencia[caracteres[i]]++;
        }
        return frequencia;
    }

    /**
     * Retorna a fila de prioridade da tabela de frequência dos caracteres.
     *
     * @param f Tabela de frequencia dos caracteres.
     * @return A fila de prioridade.
     */
    public static PriorityQueue filaPrioridade(int[] f) {
        PriorityQueue Q = new PriorityQueue();
        for (char i = 0; i < R; i++) {
            if (f[i] > 0) {
                Q.add(new No(i, f[i], null, null));
                Rn = Rn + 1;
            }
        }
        return Q;
    }

    /**
     * Constroi a tabela de símbolos.
     *
     * @param tabelaSimbolo Tabela dos símbolos.
     * @param raiz Raiz da árvore
     * @param s String concatenada
     */
    private static void constroiCodigo(String[] tabelaSimbolo, No raiz, String s) {
        if ((raiz != null) && (!raiz.eFolha())) {
            constroiCodigo(tabelaSimbolo, raiz.esquerda, s + '0');
            constroiCodigo(tabelaSimbolo, raiz.direita, s + '1');
        } else {
            if (raiz != null) {
                tabelaSimbolo[raiz.chave] = s;
            }
        }
    }

    /**
     * Executa o código de Hufmann.
     *
     * @param C Tabela da frequência dos caracteres.
     *
     * @return A raiz da árvore dos códigos.
     */
    public static Object codigoHuffman(int[] C) {
        //Qtde de caracteres
        int n = C.length;
        //Conta o número de caracteres
        Rn = 1;
        //Cria a fila de prioridade de C
        PriorityQueue Q = filaPrioridade(C);
        System.out.println("Ordem de retirada da fila de prioridade:");
        for (char i = 1; i < Rn; i++) {
            //No z = new No(); //Instanciado diretamente no local de inserção da lista de prioridade
            No esquerda = (No) Q.poll();
            int x = esquerda != null ? esquerda.frequencia : 0;            
            No direita = (No) Q.poll();            
            int y = direita != null ? direita.frequencia : 0; 
            System.out.println("Esq:" + (esquerda!=null?esquerda.chave:'*') + " f:" + x);
            System.out.println("Dir:" + (direita!=null?direita.chave:'*') + " f:" + y);            
            Q.add(new No('*', x + y, esquerda, direita));
        }        
        No raiz = (No) Q.peek();
        System.out.println("Raiz:" + (raiz!=null?raiz.chave:'*') + " f:" + raiz.frequencia);
        return Q.poll();
    }

    public static void main(String args[]) {

        System.out.println(">>> Código de Huffman com palavra de comprimento variável <<<");

        //Entrada a ser codificada
        String palavra = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbccccccccccccddddddddddddddddeeeeeeeeefffff";

        //Transforma a String em um vetor de char
        char[] caracteres = palavra.toCharArray();

        //Armazena as frequencias
        int[] C = frequencia(caracteres);

        System.out.println("Tabela de frequência:");
        mostrarFrequencia(C);

        //Retorna a raiz da árvore com os códigos apartir da tabela de frequencia C
        No raiz = (No) codigoHuffman(C);

        //Constroi a tabela de símbolos
        String[] tabelaSimbolo = new String[C.length];
        constroiCodigo(tabelaSimbolo, raiz, "");

        //Mostra a tabela de códigos.
        System.out.println("Tabela de símbolos:");
        mostrarTabelaSimbolos(tabelaSimbolo);
    }
}
