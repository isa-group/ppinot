package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.PPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Class PPIEval
 * @author resinas
 */
public class PPIEval {
    private PPI ppi;

    // Los resultados de evaluar el PPI para cada una de las medidas
    // Valores calculados de las medidas
    private List<String> valueString;
    // Porcentaje en que se satisface el PPI
    private List<Double> success;
    // Porcentaje normalizado para promediar con otros PPI
    private List<Double> normalized;

    public PPIEval(PPI ppi) {
        this.ppi = ppi;
        this.valueString = new ArrayList<String>();
        this.success = new ArrayList<Double>();
        this.normalized = new ArrayList<Double>();
    }

    public void iniValues() {

        this.valueString = new ArrayList<String>();
    }

    /**
     * Devuelve el atributo valueString:
     * Lista de String con los valores del PPI para cada uno de los periodos de tiempo evaluados
     *
     * @return Valor del atributo
     */
    public List<String> getValueString() {
        return valueString;
    }

    /**
     * Da valor al atributo valueString:
     * Lista de String con los valores del PPI para cada uno de los periodos de tiempo evaluados
     *
     * @param valueString Valor del atributo
     */
    public void setValueString(List<String> valueString) {
        this.valueString = valueString;
    }

    /**
     * Devuelve el atributo success:
     * Lista de Double que indican si se satisface PPI para cada uno de los periodos de tiempo evaluados
     *
     * @return Valor del atributo
     */
    public List<Double> getSuccess() {
        return success;
    }

    /**
     * Da valor al atributo success:
     * Lista de Double que indican si se satisface PPI para cada uno de los periodos de tiempo evaluados
     *
     * @param success Valor del atributo
     */
    public void setSuccess(List<Double> success) {
        this.success = success;
    }

    /**
     * Devuelve el atributo normalized:
     * Lista de Double con los valores del PPI para cada uno de los periodos de tiempo evaluados
     *
     * @return Valor del atributo
     */
    public List<Double> getNormalized() {
        return normalized;
    }

    /**
     * Da valor al atributo normalized:
     * Lista de Double con los valores del PPI para cada uno de los periodos de tiempo evaluados
     *
     * @param normalized Valor del atributo
     */
    public void setNormalized(List<Double> normalized) {
        this.normalized = normalized;
    }

    /**
     * Evalua el PPI
     */
    public void evaluate() {


        for (String str : this.getValueString()) {

            Double success = -1.0;
            Double normalized = -1.0;
            try {

                Double value;
                if (str.contentEquals("true"))
                    value = 1.0;
                else
                if (str.contentEquals("false"))
                    value = 0.0;
                else
                    value = Double.valueOf(str);

                if (ppi.getTarget().getRefMin()!=null && ppi.getTarget().getRefMax()!=null) {

                    // si el valor del indicador debe estar en un rango
                    if (ppi.getTarget().getRefMin()<=value && value<=ppi.getTarget().getRefMax()) {
                        success = 1.0;
                        normalized = success;
                    }
                    else {

                        if (value<ppi.getTarget().getRefMin()) {

                            success = value / ppi.getTarget().getRefMin();
                            normalized = success;
                        }
                        else {

                            success = value / ppi.getTarget().getRefMax();
                            normalized = ppi.getTarget().getRefMax() / value;
                        }
                    }
                }
                else
                if (ppi.getTarget().getRefMin()!=null) {

                    // si el valor del indicador debe ser mayor o igual a un valor dado
                    success = value / ppi.getTarget().getRefMin();
                    normalized = success;
                }
                else
                if (ppi.getTarget().getRefMax()!=null) {

                    // si el valor del indicador debe ser menor o igual a un valor dado
                    success = value / ppi.getTarget().getRefMax();
                    if (value==0)
                        normalized = 0.0;
                    else
                        normalized = ppi.getTarget().getRefMax() / value;
                }

            } catch (Exception e) {

            }

            this.getSuccess().add(success);
            this.getNormalized().add(normalized);
        }
    }


    /**
     * Indica si se debe marcar
     *
     * @param i
     * @return
     */
    public Boolean toMark(int i) {

        Boolean mark = false;

        if ( this.getSuccess().get(i)!=null ) {

            if (ppi.getTarget().getRefMin()!=null && ppi.getTarget().getRefMax()!=null) {

                // si el valor del indicador debe estar en un rango
                mark = this.getSuccess().get(i)<1 || this.getSuccess().get(i)>1;
            }
            else
            if (ppi.getTarget().getRefMin()!=null) {

                // si el valor del indicador debe ser mayor o igual a un valor dado
                mark = this.getSuccess().get(i)<1;
            }
            else
            if (ppi.getTarget().getRefMax()!=null) {

                // si el valor del indicador debe ser menor o igual a un valor dado
                mark = this.getSuccess().get(i)>1;
            }

        }

        return mark;
    }

    /**
     *
     * @return
     */
    public List<Boolean> getToMark() {

        List<Boolean> list = new ArrayList<Boolean>();
        for (int i=0; i<this.getSuccess().size(); i++) {

            list.add(this.toMark(i));
        }
        return list;
    }

    /**
     *
     * @return
     */
    public Double averageNormalize() {

        Double norm = 0.0;
        int cant = 0;
        for (Double value : this.getNormalized())
            if (value>=0) {
                norm += value;
                cant ++;
            }
        if (cant>0)
            return norm / cant;
        else
            return null;
    }

}
