public class ClassePrincipal{

    public static void main(String[] args) {
        String maxMin = "min";
        double funcaoObjetivo[] = {-1,-2};
        double matrizA[][] = {{1,1},{1,-1},{-1,1}};
        String igualdades[] = {"<=","<=","<="};
        double b[] = {6,4,4};

        Simplex2Fases simplex = new Simplex2Fases(maxMin, funcaoObjetivo, matrizA, igualdades, b);
        
        /*MatrizInversa inversa = new MatrizInversa(matrizA);

        double mInversa[][] = new double[][];
        mInversa = inversa.calculaMatrizInversa();*/
    }
}
