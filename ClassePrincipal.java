public class ClassePrincipal{

    public static void main(String[] args) {
       /* String maxMin = "min";
        double funcaoObjetivo[] = {-1,-1};
        double matrizA[][] = {{2,1},{-1,2},{3,-6}};
        String igualdades[] = {"<=","<=","<="};
        double b[] = {18,4,12};
        */
        String maxMin = "max";
        double funcaoObjetivo[] = {4,3};
        double matrizA[][] = {{1,3},{2,2},{1,1},{0,1}};
        String igualdades[] = {"<=","=","<=","<="};
        double b[] = {7,8,-3,2};

        Simplex2Fases simplex = new Simplex2Fases(maxMin, funcaoObjetivo, matrizA, igualdades, b);
        simplex.imprimeResultado();
        /*MatrizInversa inversa = new MatrizInversa(matrizA);

        double mInversa[][] = new double[][];
        mInversa = inversa.calculaMatrizInversa();*/
    }
}
