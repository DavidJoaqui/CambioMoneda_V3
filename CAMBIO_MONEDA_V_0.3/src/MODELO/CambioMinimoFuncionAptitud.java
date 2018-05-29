package MODELO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FUNDACION UNIVERSITARIA DE POPAYAN
 */
import org.jgap.*;

/**
 * Funcion de Aptitud para Cambio Minimo
 *
 */
public class CambioMinimoFuncionAptitud extends FitnessFunction {

    private final int montoObjetivo;
// Maximo monto posible 600000 mil pesos
    public static final int MAX_MONTO = 600001;
// Maxima cantidad de billetes posibles. Es igual al Monto maximo en
// pesos, ya que si se utilizan billetes de mil pesos se llegaria al
// monton con la mayor cantidad posible de billetes
    public static final int MAX_CANT_BILLETES = MAX_MONTO;
// El constructor de la funcion de aptitud debe recibir el monto objetivo
// del problema y almacenarlo en un atributo. Si el monto es invalido arroja
// una excepcion

    public CambioMinimoFuncionAptitud(int monto) {
        if (monto < 20000 || monto >= MAX_MONTO) {
            throw new IllegalArgumentException("El monto debe ser un numero entre $20.000 y " + MAX_MONTO + " pesos");
        }
        montoObjetivo = monto;
    }

    /**
     * El metodo evaluate es el metodo que se debe sobrecargar para que devuelva
     * el valor de aptitud asociado al cromosoma que se recibe por parametro.
     *
     *
     * //@param parametro de entrada el cromosoma a evaluar
     *
     * @return retorna el valor de aptitud de ese cromosoma
     *
     */
    @Override
    public double evaluate(IChromosome cromosoma) {
// Se debe tener en cuenta el evaluador que se esta usando. El evaluador
// estandar le asigna un valor mas apto a los valores mas altos de
// aptitud. Tambien hay otros evaluadores que asignan mejor aptitud a
// los valores mas bajos.
// Es por esto que se chequea si 2 es mas apto que 1. Si esto es asi
// entonces el valor mas apto sera el mayor y el menos apto el 0
        boolean evaluadorEstandard = cromosoma.getConfiguration().getFitnessEvaluator().isFitter(2, 1);
        int montoCambioBilletes = montoCambioBillete(cromosoma);
        int totalBilletes = getNumeroTotalBilletes(cromosoma);
        int diferenciaMonto = Math.abs(montoObjetivo - montoCambioBilletes);
// El primer paso es asignar la menor aptitud a aquellos cromosomas cuyo
// monto no sea el monto objetivo. Es decir una descomposicion en
// billetes que no sea del monto ingresado
        if (evaluadorEstandard) {
           
            if (diferenciaMonto != 0) {
                
                return 0.0d;
            }
           
        } else {
            if (diferenciaMonto != 0) {
                return MAX_CANT_BILLETES;
            }
        }

// luego se debe asignar mas aptitud a aquellos cromosomas que posean
// menor cantidad de billetes.
        if (evaluadorEstandard) {
// Se debe asegurar devolver un valor de aptitud positivo siempre.
// Si el valor es negativo se devuelve MAX_CANT_MONEDAS ( elemento
// menos apto )
            return Math.max(0.0d, MAX_CANT_BILLETES - totalBilletes);
        } else {
// Se debe asgurar devolver un valor de aptitud positivo siempre.
// Si el valor es negativo se devuelve 0 ( elemento menos apto )
            return Math.max(0.0d, totalBilletes);
        }
    }

    /**
     * Calcula el monto total que suman todos los billetes de un cromosoma
     *
     *
     //* @param comom parametro se recibe el cromosoma a evaluar
     * @return Retorna el monto en centimos compuesto por la suma de las monedas
     * de ese cromosoma
     *
     *
     */
    public static int montoCambioBillete(IChromosome cromosoma) {
        int BilleteCienMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 0);
        int BilleteCicuentaMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 1);
        int BilleteVeinteMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 2);
        int BilleteDiezMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 3);
        int BilleteCincoMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 4);
        int BilleteDosMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 5);
        int BilleteMil = getNumeroDeCromososmasBilletesDeGen(cromosoma, 6);

        return ((BilleteCienMil * 100000) + (BilleteCicuentaMil * 50000) + (BilleteVeinteMil * 20000) + (BilleteDiezMil * 10000) + (BilleteCincoMil * 5000) + (BilleteDosMil * 2000) + (BilleteMil * 1000));
    }

    /**
     * Calcula la cantidad de billetes de determinado tipo (gen) de un cromosoma
     * 
     * @param cromosoma El cromosoma a evaluar
     * @param numeroGen El numero gen (tipo de billete) de que se desea averiguar
     * la cantidad
     * @return Devuelve la cantidad de billetes de ese tipo de ese cromosoma
     *
     *
     * g
     */
    public static int getNumeroDeCromososmasBilletesDeGen(IChromosome cromosoma, int numeroGen) {
        Integer numBilletes = (Integer) cromosoma.getGene(numeroGen).getAllele();
        return numBilletes.intValue();
    }

    /**
     * Calcula el total de billetes que tiene esa solucion. Este valor se utiliza
     * para calcular la aptitud del cromosoma ya que el objetivo es minimizar la
     * cantidad de billetes de la solucion
     *
     *
     * @param cromosoma El cromosoma a evaluar
     * @return El total de billetes que tiene esa solucion
     *
     */
    public static int getNumeroTotalBilletes(IChromosome cromosoma) {
        int totalBilletes = 0;
        int numberOfGenes = cromosoma.size();
        for (int i = 0; i < numberOfGenes; i++) {
            totalBilletes += getNumeroDeCromososmasBilletesDeGen(cromosoma, i);
        }
        return totalBilletes;
    }
}
