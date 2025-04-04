package com.ssj;

import javafx.collections.ObservableList;

public interface Exportable {
    /**
     * Exporta los datos a un archivo de Texto plano.
     */
    void exportToText(ObservableList<?> lista);

    /**
     * Exporta los datos a un archivo PDF.
     */
    void exportToPDF();
}
