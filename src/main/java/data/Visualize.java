package data;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class Visualize extends ApplicationFrame {

    Vector<Double> bestFitnesses;
    Vector<Double> avgFitnesses;
    Vector<Double> sd;
    String chartTitle;
    public Visualize( String applicationTitle , String chartTitle ) {
        super(applicationTitle);
        bestFitnesses = new Vector<>();
        avgFitnesses = new Vector<>();
        sd = new Vector<>();
        this.chartTitle = chartTitle;
        RefineryUtilities.centerFrameOnScreen( this );
        this.setVisible( true );

        updateChart();

    }



    public void updateChart() {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Generation" ,
                "Fitness" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        ValueAxis yAxis = plot.getRangeAxis();
//        yAxis.setRange(0.0, 1.0);
        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setLowerBound(0);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.BLUE );
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
//        renderer.setSeriesShapesFilled(0, false);
//        renderer.setSeriesShapesFilled(1, false);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);

        plot.setRenderer( renderer );
        setContentPane( chartPanel );
        this.pack();

//        File file = new File("chart.png");
//        try {
//            ChartUtilities.saveChartAsPNG(file, xylineChart, 1920, 720);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void saveFile() {

    }



    private XYDataset createDataset( ) {
        final XYSeries bestFitnessSeries = new XYSeries( "Best fitness" );
        for(int i =0 ; i< bestFitnesses.size(); i++) {
            bestFitnessSeries.add(i, bestFitnesses.get(i));
        }

        final XYSeries avgFitnessSeries = new XYSeries( "Average fitness" );
        for(int i =0 ; i< avgFitnesses.size(); i++) {
            avgFitnessSeries.add(i, avgFitnesses.get(i));
        }

        final XYSeries sdSeries = new XYSeries( "Number of violation" );
        for(int i =0 ; i < sd.size(); i++) {
            sdSeries.add(i, sd.get(i));
        }


        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( bestFitnessSeries );
        dataset.addSeries( sdSeries );
        return dataset;
    }

    public void add(double bestFitness, double avgFitness, int violation) {
//        System.out.println("std " + standardDeviation);
        this.bestFitnesses.add(bestFitness);
        this.avgFitnesses.add(avgFitness);
        this.sd.add(violation * 1.0);
        updateChart();
    }

    public static void main( String[ ] args ) {

    }
}
