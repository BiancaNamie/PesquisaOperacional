import java.lang.Math;

public class MatrizInversa {
    int TAM;
    double matrizIdentidade[][];
    double matriz[][];

    public double[][] calculaMatrizInversa(double matrizOriginal[][]){
                TAM = matrizOriginal.length;
        double matrizIdentidade[][] = new double[TAM][TAM];
        double matriz[][] = new double[TAM][TAM];

        for(int i = 0; i < TAM; i++) {
            for(int j = 0; j < TAM; j++) {
                matriz[i][j] = matrizOriginal[i][j];
                if (i == j){
                    matrizIdentidade[i][j] = 1;
                }else{
                    matrizIdentidade[i][j] = 0;
                }
            }
        }
        double m;
        double pivo;
        double determinante =0;

    if (TAM == 1){ 
        determinante = determinante1x1(matriz);
    }else if (TAM == 2){
        determinante = determinante2x2(matriz);
    }else if (TAM == 3){
        determinante = determinante3x3(matriz);
    }else if (TAM >= 4){
        determinante = determinanteNxN(TAM,matriz);
    }else {
        System.out.println("Tamanho de matriz invalido");
    }

    if (determinante == 0 || determinante == -0){
        System.out.println("O determinante eh 0, nao possui matriz inversa");
        System.exit(-1);
    }else{
        //calculando matriz inversa
        for(int linha = 0; linha < TAM; linha++){
            pivo = matriz[linha][linha];
            if (pivo!= 0){
                for(int linha2 = 0; linha2 < TAM; linha2++){
                    if (linha != linha2){
                        m = matriz[linha2][linha]/pivo;
                        for(int coluna = 0; coluna <TAM; coluna++){
                            //L2 = L2 - m * L1
                            matrizIdentidade[linha2][coluna] = matrizIdentidade[linha2][coluna] - (m * matrizIdentidade[linha][coluna]);
                            matriz[linha2][coluna] = matriz[linha2][coluna] - (m * matriz[linha][coluna]);
                        }
                    }
                }
            }else {
                /// Se pivo == 0, trocar com alguma linha abaixo !=0
                int auxLinha = linha +1;
                while (matriz[auxLinha][linha] == 0){
                    auxLinha++;
                    if (auxLinha >= TAM){
                        System.out.println("ERRO");
                        System.exit(-1);
                    }
                }
                double temp;
                if(linha != auxLinha){
                    for (int i = 0; i < TAM; i++) {
                        temp = matriz[linha][i];
                        matriz[linha][i] = matriz[auxLinha][i];
                        matriz[auxLinha][i] = temp;
                        temp = matrizIdentidade[linha][i];
                        matrizIdentidade[linha][i] = matrizIdentidade[auxLinha][i];
                        matrizIdentidade[auxLinha][i] = temp;
                    }
                }
                linha--;
            }
        }


        for(int i = 0; i < TAM; i++) {
            if (matriz[i][i] < 0.0){
                inverteSinalLinha(matriz, matrizIdentidade, i);
            }
            if (matriz[i][i] != 1.0){
                divideLinha(matriz, matrizIdentidade, matriz[i][i], i);
            }
        }
    }
    return matrizIdentidade;
    }



    private static double determinanteNxN(int tamAux, double matrizA[][]) {
        double determinante = 0;

    if (tamAux == 3){
        determinante = determinante3x3(matrizA);
        return determinante;
    } else{
        for (int j = 0; j< tamAux; j++){ // escolhe sempre a linha0 para ir resolvendo
            double matrizAux[][] = new double[tamAux-1][tamAux-1];
            for (int linha = 1; linha<tamAux; linha++){
                int c=0;
                for (int coluna = 0; coluna<tamAux; coluna++){
                    if (coluna == j){
                        //faz nada
                    }else{
                        matrizAux[linha-1][c] = matrizA[linha][coluna];
                        c++;
                    }
                }
            }
            determinante += matrizA[0][j] * Math.pow(-1,(1+(j+1))) * determinanteNxN( tamAux-1, matrizAux);
        }
    }
    return determinante;
    }

    private static double determinante3x3(double matriz[][]){ //Saurus
    double diagonalPrincipal = 0;
    double diagonalSecundaria = 0;
    double determinante = 0;

    diagonalPrincipal = matriz[0][0]*matriz[1][1]*matriz[2][2] + matriz[0][1]*matriz[1][2]*matriz[2][0] + matriz[0][2]*matriz[1][0]*matriz[2][1];
    diagonalSecundaria = matriz[0][2]*matriz[1][1]*matriz[2][0] + matriz[0][0]*matriz[1][2]*matriz[2][1] + matriz[0][1]*matriz[1][0]*matriz[2][2];

    determinante = diagonalPrincipal - diagonalSecundaria;

    return determinante;
    }

    private static double determinante2x2(double matriz[][]){
    double determinante = 0;
    determinante = (matriz[0][0]*matriz[1][1]) - (matriz[0][1]*matriz[1][0]);
    return determinante;
    }

    private static double determinante1x1(double matriz[][]){
        return matriz[0][0];
    }

    private static void inverteSinalLinha(double matriz[][], double matrizIdentidade[][], int linha){
    for(int coluna = 0; coluna <matriz[0].length; coluna++) {
        matrizIdentidade[linha][coluna] *= -1;
        matriz[linha][coluna] *= -1;
    }
}

private static void divideLinha(double matriz[][], double matrizIdentidade[][], double pivo, int linha){
    for(int coluna = 0; coluna <matriz[0].length; coluna++){  //matriz[0].length pega o num de colunas
        matrizIdentidade[linha][coluna] /= pivo;
        matriz[linha][coluna] /= pivo;
    }
}


}