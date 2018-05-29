package MODELO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static VISTA.VISTA.jPr;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;
/**
 *
 * @author HUAWEI-ADMIN
 */
public class CambioMinimo {
    /**
* The total number of times we'll let the population evolve.
*/
public static final int MAX_EVOLUCIONES_PERMITIDAS = 1500;
public static String cadena="";

/**
* Calcula utilizando algoritmos geneticos la solucion al problema y la
* imprime por pantalla
*
* @param Monto
* Monto que se desea descomponer en la menor cantidad de monedas
* posibles
* @throws Exception
*
*/
public static void calcularCambioMinimo(int Monto)throws Exception {

// Se crea una configuracion con valores predeterminados.

// -------------------------------------------------------------
Configuration conf = new DefaultConfiguration();
// Se indica en la configuracion que el elemento mas apto siempre pase a
// la proxima generacion
// -------------------------------------------------------------
conf.setPreservFittestIndividual(true);
// Se Crea la funcion de aptitud y se setea en la configuracion
// ---------------------------------------------------------
FitnessFunction miFuncion = new CambioMinimoFuncionAptitud(Monto);
conf.setFitnessFunction(miFuncion);
// Ahora se debe indicar a la configuracion como seran los cromosomas: en
// este caso tendran 7 genes (uno para cada tipo de billete) con un valor
// entero (cantidad de billetes de ese tipo).
// Se debe crear un cromosoma de ejemplo y cargarlo en la configuracion
// Cada gen tendra un valor maximo y minimo que debe setearse.
// --------------------------------------------------------------
Gene[] sampleGenes = new Gene[7];
sampleGenes[0] = new IntegerGene(conf, 0, Math.round(CambioMinimoFuncionAptitud.MAX_MONTO/100000)); // billete 100000 mil pesos
sampleGenes[1] = new IntegerGene(conf, 0, 10); // billete 50 milpesos
sampleGenes[2] = new IntegerGene(conf, 0, 10); // billete 20 mil pesos
sampleGenes[3] = new IntegerGene(conf, 0, 10); // billete 10 mil pesos
sampleGenes[4] = new IntegerGene(conf, 0, 10); // billete  5 mil pesos
sampleGenes[5] = new IntegerGene(conf, 0, 10); // billete  2 mil pesos
sampleGenes[6] = new IntegerGene(conf, 0, 10); // billete  1 mil pesos
IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
conf.setSampleChromosome(sampleChromosome);
// Por ultimo se debe indicar el tamaño de la poblacion en la
// configuracion
// ------------------------------------------------------------
conf.setPopulationSize(2000);
Genotype Poblacion;
// El framework permite obtener la poblacion inicial de archivos xml
// pero para este caso particular resulta mejor crear una poblacion
// aleatoria, para ello se utiliza el metodo randomInitialGenotype que
// devuelve la poblacion random creada
Poblacion = Genotype.randomInitialGenotype(conf);
// La Poblacion debe evolucionar para obtener resultados mas aptos
// ---------------------------------------------------------------
long TiempoComienzo = System.currentTimeMillis();
for (int i = 0; i < MAX_EVOLUCIONES_PERMITIDAS; i++) {
Poblacion.evolve(); 
}
long TiempoFin = System.currentTimeMillis();
cadena+="Tiempo total de evolucion: " + (TiempoFin - TiempoComienzo) + " ms -- "+(TiempoFin - TiempoComienzo)/1000+"s";
guardarPoblacion(Poblacion);
// Una vez que la poblacion evoluciono es necesario obtener el cromosoma
// mas apto para mostrarlo como solucion al problema planteado para ello
// se utiliza el metodo getFittestChromosome
IChromosome cromosomaMasApto = Poblacion.getFittestChromosome();
if(Monto==CambioMinimoFuncionAptitud.montoCambioBillete(cromosomaMasApto)){
cadena+="\n"+"El cromosoma mas apto encontrado tiene un valor de aptitud de: " + cromosomaMasApto.getFitnessValue();
cadena+="\n"+"Y esta formado por la siguiente distribucion de Billetes: ";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 0) + " Billete(s) 100 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 1) + " Billete(s)  50 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 2) + " Billete(s)  20 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 3) + " Billete(s)  10 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 4) + " Billete(s)   5 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 5) + " Billete(s)   2 mil pesos";
cadena+="\n"+"\t" + CambioMinimoFuncionAptitud.getNumeroDeCromososmasBilletesDeGen(cromosomaMasApto, 6) + " Billete(s)     mil pesos";
cadena+="\n"+"Para un total de "+ CambioMinimoFuncionAptitud.montoCambioBillete(cromosomaMasApto) + " pesos " + CambioMinimoFuncionAptitud.getNumeroTotalMonedas(cromosomaMasApto) + " Billetes.";
}else{
    JOptionPane.showMessageDialog(null,"No se encontro cromosoma más apto");
}
}
/**
* Metodo principal: Recibe el monto en dinero por parametro para determinar
* la cantidad minima de monedas necesarias para formarlo
*
*
     * @param Poblacion
     * @throws java.lang.Exception
*/

// ---------------------------------------------------------------------
// Este metodo permite guardar en un xml la ultima poblacion calculada
// ---------------------------------------------------------------------
public static void guardarPoblacion(Genotype Poblacion) throws Exception {
DataTreeBuilder builder = DataTreeBuilder.getInstance();
IDataCreators doc2 = builder.representGenotypeAsDocument(Poblacion);
// create XML document from generated tree
XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
XMLManager.writeFile(xmlDoc, new File("PoblacionCambioMinimo.xml"));
}


public static void inicio(int monto) {
        //Creamos un Thread para mejorar el ejemplo        
        jPr.setValue(50);
        try {

            CambioMinimo.calcularCambioMinimo(monto);
            final Thread t;
            //Inicializamos
            t = new Thread(new Runnable() {
                //Implementamos el método run()
                @Override
                public void run() {
                    //Permite mostrar el valor del progreso
                    jPr.setStringPainted(true);
                    int x = 1;
                    //Utilizamos un while para emular el valor mínimo y máximo
                    //En este caso 0 - 100
                    while (x <= MAX_EVOLUCIONES_PERMITIDAS) {
                        //Asignamos valor a nuestro JProgressBar por cada ciclo del bucle                        
                        jPr.setValue(x);   
                        //Valor que se mostrará en el JTextArea
                        //salida.append("Progreso " + x + "%...\n");
                        //Hacemos una parada de medio segundo por cada ciclo while
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                        }
                        //Se incrementa el valor de x
                        x++;
                    }

                }

            });
            //Se ejecuta el Thread
           
            t.start();
            VISTA.VISTA.jTextArea1.setText(CambioMinimo.cadena);
            
            CambioMinimo.cadena = "";
            Configuration.reset();

        } catch (Exception e) {
            System.out.println("Error...\n" + e);
        }
    }

}

