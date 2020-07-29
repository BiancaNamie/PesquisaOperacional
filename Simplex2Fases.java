public class Simplex2Fases {
    int quantidadeVariaveis, quantidadeRestricoes, TAM_FolgaExcesso;
    double funcObj[];

    double vetorb[][];

    double matriz_A[][];

    // var p FaseII
    int particaoBasica[];
    int particaoNaoBasica[];
    double matrizBasicos[][];
    double matrizNaoBasicos[][];
    double matrizInversa[][];

    boolean flag;

    //resultado
    double Z;
    double vetorX[];

    // InicializaProblema
    Simplex2Fases(String maxMin, double funcaoObj[], double matrizA[][], String igualdades[], double b[]) {
        flag = false;
        quantidadeVariaveis = matrizA[0].length;
        quantidadeRestricoes = matrizA.length;
        TAM_FolgaExcesso = quantidadeRestricoes;
        vetorb = new double[quantidadeRestricoes][1];

        for (int i = 0; i < quantidadeRestricoes; i++) {
            if (b[i] >= 0) {
                vetorb[i][0] = b[i];
            } else {
                vetorb[i][0] = b[i] * (-1);
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
                System.out.println("Entrou?");
                TAM_FolgaExcesso--;
                flag = true;
            } else if (igualdades[i].equalsIgnoreCase(">") || igualdades[i].equalsIgnoreCase(">=")) {
                flag = true;
            }
        }

        this.funcObj = new double[quantidadeVariaveis + TAM_FolgaExcesso];

        if (maxMin.equalsIgnoreCase("max")) {
            for (int i = 0; i < quantidadeVariaveis + TAM_FolgaExcesso; i++) {
                if (i < funcaoObj.length) {
                    this.funcObj[i] = funcaoObj[i] * (-1);
                } else {
                    this.funcObj[i] = 0;
                }
            }
        } else {
            for (int i = 0; i < quantidadeVariaveis + TAM_FolgaExcesso; i++) {
                if (i < funcaoObj.length) {
                    this.funcObj[i] = funcaoObj[i];
                } else {
                    this.funcObj[i] = 0;
                }
            }
        } // end max min ajuste func objetivo

        int aux = quantidadeVariaveis;
        System.out.println(quantidadeRestricoes + " " + quantidadeVariaveis);
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
        for (int i = 0; i < quantidadeRestricoes; i++) {
            for (int j = 0; j < quantidadeVariaveis + TAM_FolgaExcesso; j++) {
                System.out.print(" " + matriz_A[i][j]);
            }
            System.out.print(" =" + vetorb[i][0]);
            System.out.println();
        }

        if (flag == false) {
            FaseII();
        } else {
            FaseI();
        }
    }

    public void FaseI() {
        double newFuncObjetivo[] = new double[quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes];
        for (int i = 0; i < quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes; i++) {
            if (i < quantidadeVariaveis + TAM_FolgaExcesso) {
                newFuncObjetivo[i] = 0;
            } else {
                newFuncObjetivo[i] = 1;
            }
        }

        double newMatrizA[][] = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso
                + quantidadeRestricoes];
        for (int i = 0; i < quantidadeRestricoes; i++) {
            for (int j = 0; j < quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes; j++) {
                if (j < quantidadeVariaveis + TAM_FolgaExcesso) {
                    newMatrizA[i][j] = matriz_A[i][j];
                } else {
                    if (i == j + quantidadeVariaveis + TAM_FolgaExcesso) {
                        newMatrizA[i][j] = 1;
                    } else {
                        newMatrizA[i][j] = 0;
                    }
                }
            }
        }
        
        int particaoBasicaA[] = new int[quantidadeRestricoes];
        int particaoNaoBasicaA[] = new int[quantidadeVariaveis + TAM_FolgaExcesso];
        for (int i = 0, aux0=0; i < quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes; i++) {
            if (i < quantidadeVariaveis + TAM_FolgaExcesso) {
                particaoNaoBasicaA[i] = i;
            } else {
                particaoBasicaA[aux0] = i;
                aux0++;
            }
        }

       double matrizBasicosA[][] = new double[quantidadeRestricoes][quantidadeRestricoes];
       double matrizNaoBasicosA[][] = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso];        
        for (int i = 0; i < quantidadeRestricoes; i++) {
            for (int j = 0; j < quantidadeVariaveis + TAM_FolgaExcesso + quantidadeRestricoes; j++) {
                if (j < quantidadeVariaveis + TAM_FolgaExcesso) {
                    matrizNaoBasicos[i][j] = matriz_A[i][j];
                } else {
                    matrizBasicos[i][j - quantidadeVariaveis + TAM_FolgaExcesso] = matriz_A[i][j];
                }
            }
        }

        matrizInversa = new double[quantidadeRestricoes][quantidadeRestricoes];

        int Iteracao = 0;
        while (true){
            // ->Passo 1
            MatrizInversa mi = new MatrizInversa();
            System.out.println();
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < quantidadeVariaveis; j++) {
                    System.out.print(" " + matrizNaoBasicosA[i][j]);
                }
                System.out.println();
            }
            System.out.println();
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < TAM_FolgaExcesso; j++) {
                    System.out.print(" " + matrizBasicosA[i][j]);
                }
                System.out.println();
            }

            matrizInversa = mi.calculaMatrizInversa(matrizBasicosA);
            System.out.println("Matriz Inversa");
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < quantidadeRestricoes; j++) {
                    System.out.println(" " + matrizInversa[i][j]);
                }
            }
            System.out.println();

            double xb[][] = multiplicaMatriz(matrizInversa, vetorb);

            // -> Passo 2 ctb = funcao Objetivo transposta dos valores básicos
            double cTB[][] = new double[1][quantidadeRestricoes];
            for (int i = 0; i < quantidadeRestricoes; i++) {
                cTB[0][i] = newFuncObjetivo[particaoBasicaA[i]];
            }
            // ->2.1
            double lambda[][] = multiplicaMatriz(cTB, matrizInversa);
            System.out.println("newFuncObjetivo: " + newFuncObjetivo[particaoBasicaA[0]] + " " + newFuncObjetivo[particaoBasicaA[1]] + " "
                    + newFuncObjetivo[particaoBasicaA[2]]);
            System.out.println("Lambda: " + lambda[0][0] + " " + lambda[0][1] + " " + lambda[0][2]);
            // ->2.2 custos relativos
            double cNj[] = new double[quantidadeVariaveis];
            for (int i = 0; i < quantidadeVariaveis; i++) {
                double aux[][] = new double[quantidadeRestricoes][1];
                for (int j = 0; j < quantidadeRestricoes; j++) {
                    aux[j][0] = newMatrizA[j][particaoNaoBasicaA[i]];
                }
                // System.out.println();
                double aux2[][] = multiplicaMatriz(lambda, aux);
                cNj[i] = newFuncObjetivo[particaoNaoBasicaA[i]] - aux2[0][0];
                // System.out.println(cNj[i] + " = " + newFuncObjetivo[particaoNaoBasicaA[i]] +" - " +
                // aux2[0][0]);
            }
            // ->2.3
            int entraNaBase = 0;
            for (int i = 1; i < quantidadeVariaveis; i++) {
                if (cNj[i] < cNj[entraNaBase]) {
                    entraNaBase = i;
                }
            }
            System.out.println("Entra na bas: " + entraNaBase);
            // -> Passo 3
            System.out.println(cNj[entraNaBase]);
            if (cNj[entraNaBase] >= 0) {
                break;
                // Soluçao ótima parar
            }
            // -> Passo 4 calculo da direcao simplex
            double y[][] = new double[quantidadeRestricoes][1];
            double aNk[][] = new double[quantidadeRestricoes][1];
            // for (int i = 0; i < quantidadeVariaveis; i++) {
            for (int j = 0; j < quantidadeRestricoes; j++) {
                aNk[j][0] = newMatrizA[j][particaoNaoBasicaA[entraNaBase]];
            }
            // }
            y = multiplicaMatriz(matrizInversa, aNk);

            // -> Passo 5 determinacao do passo e variavel a sair da base
            int saiDaBase = -1;
            double[] razaoMinima = new double[quantidadeRestricoes];
            for (int i = 0; i < quantidadeRestricoes; i++) {
                if (y[i][0] > 0) {
                    razaoMinima[i] = xb[i][0] / y[i][0];
                    System.out.println("Raz min de " + i + " = " + razaoMinima[i] + " y= " + y[i][0]);
                    if (razaoMinima[i] > 0) {
                        if (saiDaBase == -1) {
                            saiDaBase = i;
                        } else if (razaoMinima[i] < razaoMinima[saiDaBase]) {
                            saiDaBase = i;
                        }
                    }
                } else {
                    razaoMinima[i] = 0;
                }
            }
            if (saiDaBase == -1) {
                System.out.println("problema nao tem solucao otima finita f(x) → −∞");
                System.exit(-1);
            }
            System.out.println("sai da bse:  " + saiDaBase);
            // -> Passo 6
            double aux4[][] = new double[quantidadeRestricoes][1];
            int aux3 = particaoNaoBasicaA[entraNaBase];
            particaoNaoBasicaA[entraNaBase] = particaoBasicaA[saiDaBase];
            particaoBasicaA[saiDaBase] = aux3;
            for (int i = 0; i < quantidadeRestricoes; i++) {
                aux4[i][0] = matrizNaoBasicosA[i][entraNaBase];
                matrizNaoBasicosA[i][entraNaBase] = matrizBasicosA[i][saiDaBase];
                matrizBasicosA[i][saiDaBase] = aux4[i][0];
            }
            Iteracao++;
            System.out.println("Iteracao: " + Iteracao);
            if (Iteracao > 5) {
                break;
            }
        }

        }
    public double[][] FaseII() {
        particaoBasica = new int[quantidadeVariaveis + TAM_FolgaExcesso];
        particaoNaoBasica = new int[quantidadeRestricoes];
        matrizBasicos = new double[quantidadeRestricoes][quantidadeVariaveis + TAM_FolgaExcesso];
        matrizNaoBasicos = new double[quantidadeRestricoes][quantidadeRestricoes];
        matrizInversa = new double[quantidadeRestricoes][quantidadeRestricoes];
        int aux0 = 0;
        for (int i = 0; i < quantidadeVariaveis + TAM_FolgaExcesso; i++) {
            if (i < quantidadeVariaveis) {
                particaoNaoBasica[i] = i;
            } else {
                particaoBasica[aux0] = i;
                aux0++;
            }
        }
        for (int i = 0; i < quantidadeRestricoes; i++) {
            for (int j = 0; j < quantidadeVariaveis + TAM_FolgaExcesso; j++) {
                if (j < quantidadeVariaveis) {
                    matrizNaoBasicos[i][j] = matriz_A[i][j];
                } else {
                    matrizBasicos[i][j - quantidadeVariaveis] = matriz_A[i][j];
                }
            }
        }

        int Iteracao = 0;
        while (true) {
            // ->Passo 1
            MatrizInversa mi = new MatrizInversa();
            System.out.println();
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < quantidadeVariaveis; j++) {
                    System.out.print(" " + matrizNaoBasicos[i][j]);
                }
                System.out.println();
            }
            System.out.println();
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < TAM_FolgaExcesso; j++) {
                    System.out.print(" " + matrizBasicos[i][j]);
                }
                System.out.println();
            }

            matrizInversa = mi.calculaMatrizInversa(matrizBasicos);
            System.out.println("Matriz Inversa");
            for (int i = 0; i < quantidadeRestricoes; i++) {
                for (int j = 0; j < quantidadeRestricoes; j++) {
                    System.out.println(" " + matrizInversa[i][j]);
                }
            }
            System.out.println();

            double xb[][] = multiplicaMatriz(matrizInversa, vetorb);

            // -> Passo 2 ctb = funcao Objetivo transposta dos valores básicos
            double cTB[][] = new double[1][quantidadeRestricoes];
            for (int i = 0; i < quantidadeRestricoes; i++) {
                cTB[0][i] = funcObj[particaoBasica[i]];
            }
            // ->2.1
            double lambda[][] = multiplicaMatriz(cTB, matrizInversa);
            System.out.println("FuncObj: " + funcObj[particaoBasica[0]] + " " + funcObj[particaoBasica[1]] + " "
                    + funcObj[particaoBasica[2]]);
            System.out.println("Lambda: " + lambda[0][0] + " " + lambda[0][1] + " " + lambda[0][2]);
            // ->2.2 custos relativos
            double cNj[] = new double[quantidadeVariaveis];
            for (int i = 0; i < quantidadeVariaveis; i++) {
                double aux[][] = new double[quantidadeRestricoes][1];
                for (int j = 0; j < quantidadeRestricoes; j++) {
                    aux[j][0] = matriz_A[j][particaoNaoBasica[i]];
                }
                // System.out.println();
                double aux2[][] = multiplicaMatriz(lambda, aux);
                cNj[i] = funcObj[particaoNaoBasica[i]] - aux2[0][0];
                // System.out.println(cNj[i] + " = " + funcObj[particaoNaoBasica[i]] +" - " +
                // aux2[0][0]);
            }
            // ->2.3
            int entraNaBase = 0;
            for (int i = 1; i < quantidadeVariaveis; i++) {
                if (cNj[i] < cNj[entraNaBase]) {
                    entraNaBase = i;
                }
            }
            System.out.println("Entra na bas: " + entraNaBase);
            // -> Passo 3
            System.out.println(cNj[entraNaBase]);
            if (cNj[entraNaBase] >= 0) {
                break;
                // Soluçao ótima parar
            }
            // -> Passo 4 calculo da direcao simplex
            double y[][] = new double[quantidadeRestricoes][1];
            double aNk[][] = new double[quantidadeRestricoes][1];
            // for (int i = 0; i < quantidadeVariaveis; i++) {
            for (int j = 0; j < quantidadeRestricoes; j++) {
                aNk[j][0] = matriz_A[j][particaoNaoBasica[entraNaBase]];
            }
            // }
            y = multiplicaMatriz(matrizInversa, aNk);

            // -> Passo 5 determinacao do passo e variavel a sair da base
            int saiDaBase = -1;
            double[] razaoMinima = new double[quantidadeRestricoes];
            for (int i = 0; i < quantidadeRestricoes; i++) {
                if (y[i][0] > 0) {
                    razaoMinima[i] = xb[i][0] / y[i][0];
                    System.out.println("Raz min de " + i + " = " + razaoMinima[i] + " y= " + y[i][0]);
                    if (razaoMinima[i] > 0) {
                        if (saiDaBase == -1) {
                            saiDaBase = i;
                        } else if (razaoMinima[i] < razaoMinima[saiDaBase]) {
                            saiDaBase = i;
                        }
                    }
                } else {
                    razaoMinima[i] = 0;
                }
            }
            if (saiDaBase == -1) {
                System.out.println("problema nao tem solucao otima finita f(x) → −∞");
                System.exit(-1);
            }
            System.out.println("sai da bse:  " + saiDaBase);
            // -> Passo 6
            double aux4[][] = new double[quantidadeRestricoes][1];
            int aux3 = particaoNaoBasica[entraNaBase];
            particaoNaoBasica[entraNaBase] = particaoBasica[saiDaBase];
            particaoBasica[saiDaBase] = aux3;
            for (int i = 0; i < quantidadeRestricoes; i++) {
                aux4[i][0] = matrizNaoBasicos[i][entraNaBase];
                matrizNaoBasicos[i][entraNaBase] = matrizBasicos[i][saiDaBase];
                matrizBasicos[i][saiDaBase] = aux4[i][0];
            }
            Iteracao++;
            System.out.println("Iteracao: " + Iteracao);
            if (Iteracao > 5) {
                break;
            }
        }
        return matriz_A;
    }

    private static void inverteSinalLinha(double matriz[][], int linha) {
        for (int coluna = 0; coluna < matriz[0].length; coluna++) {
            matriz[linha][coluna] *= -1;
        }
    }

    private static double[][] multiplicaMatriz(double mA[][], double mB[][]) {

        double matrizC[][] = new double[mA.length][mB[0].length];
        for (int i = 0; i < matrizC.length; i++) {
            for (int j = 0; j < matrizC[i].length; j++) {
                for (int k = 0; k < mA[i].length; k++) {
                    matrizC[i][j] += mA[i][k] * mB[k][j];
                }
            }
        }
        return matrizC;
    }

}