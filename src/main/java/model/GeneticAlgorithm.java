package model;

import lib.Slot;
import lib.SlotGroup;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

        public class GeneticAlgorithm {
//            public static final int POPULATION_SIZE = 100;
//            public static final double MUTATION_RATE = 0.25;
//            public static final int TOURNAMENT_SIZE = 3;
            public static final int CLASS_NUMBER = 5;
            public static final double IN_CLASS_RATE = 0.9;
//        public static final double

            Population population;
            InputData inputData;
        private int generation;
        private Train train;
        public GeneticAlgorithm(InputData inputData, Train train) {
            this.generation = 0;
            this.inputData = inputData;
            this.train = train;
            this.population = new Population(this.inputData.getGaParameter().getPopulationSize(), inputData);
        }
        public void updateFitness() {
            this.population.updateFitness();

            //print current generation info

            this.generation++;
//            System.out.println("asdfasdf" + this.population.get);

//            System.out.println("Fitness Average: " + this.population.getAverageFitness());
//            System.out.println("Best fitness: "  + this.population.getBestIndividuals().getFitness());
//            System.out.println("Generation: " + this.generation);
            Chromosome best = this.population.getBestIndividuals();
            this.train.notify(best, this.generation, best.getFitness(), this.population.getAverageFitness(),
                    best.getNumberOfViolation(), best.calculateObjectiveFunction());
            this.population.getBestIndividuals().display();
//            if (this.generation % 100 == 0) {
//                DataWriter.writeToCsv(this.model, this.population.getBestIndividuals(), "result" + this.generation / 100 + ".csv");
//            }
        }


    public Chromosome selectParent() {
        Random random = new Random();
        Vector<Chromosome> candidates = new Vector<>();
        for(int i = 0; i < this.inputData.getGaParameter().getTournamentSize(); i++) {
            int idx = random.nextInt(this.inputData.getGaParameter().getPopulationSize());
            candidates.add(this.population.getIndividuals().get(idx));
        }

        double best = candidates.get(0).getFitness();
        Chromosome res = candidates.get(0);
        for(Chromosome chromosome:candidates) {
            if (chromosome.getFitness() > best) {
                best = chromosome.getFitness();
                res = chromosome;
            }
        }
        return res;
    }

    public Chromosome selectParentRandomly() {
        Random random = new Random();
        int idx = random.nextInt(this.inputData.getGaParameter().getPopulationSize());
        return this.population.getIndividuals().get(idx);
    }

    public Chromosome selectParent(Vector<Chromosome> individuals) {
        Random random = new Random();
        Vector<Chromosome> candidates = new Vector<>();
        for(int i = 0; i < this.inputData.getGaParameter().getTournamentSize(); i++) {
            int idx = random.nextInt(individuals.size());
            candidates.add(individuals.get(idx));
        }

        double best = candidates.get(0).getFitness();
        Chromosome res = candidates.get(0);
        for(Chromosome chromosome:candidates) {
            if (chromosome.getFitness() > best) {
                best = chromosome.getFitness();
                res = chromosome;
            }
        }
        return res;
    }

    public void selection1() {
        Population population1 = new Population(this.inputData);
        for(int i = 0; i < this.inputData.getGaParameter().getPopulationSize() / 2 ; i++) {
            Chromosome p1 = selectParent();
            Chromosome p2 = selectParent();
            Chromosome c1 = this.crossover(p1, p2);
            Chromosome c2 = this.crossover(p2, p1);
            population1.addIndividual(c1);
            population1.addIndividual(c2);
        }
        this.population = population1;
    }

    public void selection() {
        Population population1 = new Population(this.inputData);

        this.population.sortByFitnetss();
        Vector<Vector<Chromosome>> individualsByClass = new Vector();
        for(int i = 0 ; i < CLASS_NUMBER; i++) {
            individualsByClass.add(new Vector());
        }


        int classSize = this.inputData.getGaParameter().getPopulationSize() / CLASS_NUMBER + ((this.inputData.getGaParameter().getPopulationSize() % CLASS_NUMBER == 0) ? 0 : 1);
        for(int i = 0; i < this.inputData.getGaParameter().getPopulationSize(); i++) {
            int classId = i / classSize;
            individualsByClass.get(classId).add(this.population.getIndividuals().get(i));
        }

        for(int i = 0 ; i < CLASS_NUMBER; i++) {
            Collections.shuffle(individualsByClass.get(i));
        }


        int inClassPairNumber = (int) (this.inputData.getGaParameter().getPopulationSize() * IN_CLASS_RATE /  CLASS_NUMBER / 2);
        for(int i = 0; i < CLASS_NUMBER; i++) {
            for(int j = 0 ; j < inClassPairNumber; j++) {
                Chromosome p1 = selectParent(individualsByClass.get(i));
                Chromosome p2 = selectParent(individualsByClass.get(i));
                Chromosome c1 = this.crossover(p1, p2);
                Chromosome c2 = this.crossover(p2, p1);
                population1.addIndividual(c1);
                population1.addIndividual(c2);
            }
        }



        while (population1.getSize() < this.inputData.getGaParameter().getPopulationSize()) {
            Chromosome p1 = selectParentRandomly();
            Chromosome p2 = selectParentRandomly();
            Chromosome c1 = this.crossover(p1, p2);
            Chromosome c2 = this.crossover(p2, p1);
            population1.addIndividual(c1);
            population1.addIndividual(c2);
        }
        this.population = population1;
    }
        public Chromosome crossover(Chromosome c1, Chromosome c2) {
            Vector<Slot> slots = SlotGroup.getSlotList(this.inputData.getSlots());
            Vector<Vector<Integer>> genes = new Vector<>();
            Random random = new Random();
            for(Slot slot:slots) {
                Vector<Integer> p1 = c1.getGenes().get(slot.getId());
                Vector<Integer> p2 = c2.getGenes().get(slot.getId());
                Vector<Integer> p3 = (new PMX(p1, p2, random.nextInt(Integer.MAX_VALUE))).getChildren();

                genes.add(p3);
            }

            return new Chromosome(this.inputData, genes);
        }
        public void mutate() {
        Random random = new Random();
        for(int i = 0; i < this.inputData.getGaParameter().getPopulationSize(); i++) {
            for(int j = 0;  j < 1; j++) {
                if (random.nextDouble() < this.inputData.getGaParameter().getMutationRate()) {
                    this.population.getIndividuals().get(i).mutate();
                }
            }
            this.population.getIndividuals().get(i).autoRepair();
        }
    }
    public void start() {
            int stop_times = 50;
            double temp = 0;
            int count = 0;
        while (count < stop_times) {
            this.updateFitness();
            this.selection1();
            this.mutate();
            if(temp < this.population.getBestIndividuals().getFitness()){
                count = 0;
            }
            else if(temp == this.population.getBestIndividuals().getFitness()){
                count ++;
            }
//            System.out.println(this.population.getBestIndividuals().getSchedule());
            temp = this.population.getBestIndividuals().getFitness();
        }
        System.out.println(this.population.getBestIndividuals().getGenes());
        System.out.println(this.population.getBestIndividuals().getFitness());
    }
}
