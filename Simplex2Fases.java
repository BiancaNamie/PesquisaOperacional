public class Simplex2Fases {
    int quantidadeVariaveis, quantidadeRestricoes, TAM_FolgaExcesso;
    double funcObj[];
    double newFuncObjetivo[]; // Caso precise na FASE I
    double vetorb[][];

    double matriz_A[][];

    boolean flag;

    // InicializaProblema
    Simplex2Fases(String maxMin, double funcaoObj[], double matrizA[][], String igualdades[], double b[]) {
        flag = false;
        quantidadeVariaveis = matrizA[0].length;
        quantidadeRestricoes = matrizA.length;
        TAM_FolgaExcesso = quantidadeRestricoes;
        vetorb = new double[1][quantidadeRestricoes];

        for (int i = 0; i < quantidadeRestricoes; i++) {
            if (b[i] >= 0) {
                vetorb[0][i] = b[i];
            } else {
                vetorb[0][i] = b[i] * (-1);
                if (igualdades[i].equalsIgnoreCase(">")) {
                    igualdades[i] = "<";
                } else if (igualdades[i].equalsIgnoreCase(">=")) {
                    igualdades[i] = "<=";
                } else if (igualdades[i].equalsIgnoreCase("<=")) {
                    igualdades[i] = ">=";
                } else if (igualdades[i].equalsIgnoreCase("<")) {
                    igualdades[i] = "<";
                }
                inverteSinalLinha(matrizA, i);
            }
        }

        for (int i = 0; i < quantidadeRestricoes; i++) {
            if (igualdades[i].equalsIgnoreCase("=")) {
                TAM_FolgaExcesso--;
                flag = true;
            } else if (igualdades[i].equalsIgnoreCase(">") || igualdades[i].equalsIgnoreCase(">=")) {
                flag = true;
            }
        }

        this.funcObj = new double[quantidadeVariaveis + TAM_FolgaExcesso];

        if (maxMin.equalsIgnoreCase("max")) {
            for (int i = 0; i < quantidadeVariaveis+TAM_FolgaExcesso; i++) {
                if (i < funcaoObj.length) {
                    this.funcObj[i] = funcaoObj[i] * (-1);
                } else {
                    this.funcObj[i] = 0;
                }
            }
        } else {
            for (int i = 0; i < quantidadeVariaveis+TAM_FolgaExcesso; i++) {
                if (i < funcaoObj.length) {
                    this.funcObj[i] = funcaoObj[i];
                } else {
                    this.funcObj[i] = 0;
                }
            }
        } // end max min ajuste func objetivo

        int aux = TAM_FolgaExcesso;
        System.out.println(quantidadeRestricoes + " " +quantidadeVariaveis);
        matriz_A = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso];
        for (int i = 0; i < quantidadeRestricoes; i++) {
            for (int j = 0; j < quantidadeVariaveis + TAM_FolgaExcesso; j++) {
                if (j < matrizA[0].length) {
                    matriz_A[i][j] = matrizA[i][j];
                } else {
                    matriz_A[i][j] = 0;
                }
            }
                if (igualdades[i].equalsIgnoreCase("<") || igualdades[i].equalsIgnoreCase("<=")) {
                    matriz_A[i][aux] = 1;
                } else if (igualdades[i].equalsIgnoreCase(">") || igualdades[i].equalsIgnoreCase(">=")) {
                    matriz_A[i][aux] = -1;
                    flag = true;
                } else if (igualdades[i].equalsIgnoreCase("=")) {
                    flag = true;
                }
                aux++;
        }
        for (int i = 0; i < quantidadeRestricoes;i++){
            for (int j=0;j < quantidadeVariaveis + TAM_FolgaExcesso;j++){
                System.out.print(" " + matriz_A[i][j]);
            }
            System.out.print(" =" + vetorb[0][i]);
            System.out.println();
        }
    }

    public void FaseI() {
        double newFuncObjetivo[] = new double[quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes];
        double newMatrizA[][] = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso
                + quantidadeRestricoes];
        double matrizArtificiais[][] = new double[quantidadeRestricoes][quantidadeRestricoes];
        double particaoBasica[] = new double[quantidadeVariaveis + TAM_FolgaExcesso];
        double particaoNaoBasica[] = new double[quantidadeRestricoes];
        double matrizBasicos[][] = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso];
        double matrizNaoBasicos[][] = new double[quantidadeRestricoes][quantidadeRestricoes];
        double matrizInversa[][] = new double[quantidadeRestricoes][quantidadeRestricoes];
        double xb[][] = new double[quantidadeRestricoes][quantidadeRestricoes];

    }

    private static void inverteSinalLinha(double matriz[][], int linha) {
        for (int coluna = 0; coluna < matriz[0].length; coluna++) {
            matriz[linha][coluna] *= -1;
        }
    }
}